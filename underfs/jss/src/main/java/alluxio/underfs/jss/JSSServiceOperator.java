/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0 (the
 * “License”). You may not use this work except in compliance with the License, which is available
 * at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio.underfs.jss;

import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcloud.jss.Credential;
import com.jcloud.jss.JingdongStorageService;
import com.jcloud.jss.client.ClientConfig;

import alluxio.Configuration;
import alluxio.Constants;

/**
 *
 */
public class JSSServiceOperator implements IReferencer {
  private static final Logger LOG = LoggerFactory.getLogger(JSSServiceOperator.class);

  /**
   * Reference count.
   */
  private AtomicInteger mJSSClientRefs = new AtomicInteger(0);

  /**
   * JSS client for user.
   */
  private JingdongStorageService mJSSClient = null;

  private Object mClientAliveLockObj = new Object();

  /**
   * Delay destroy timer.
   */
  private Timer mTimer = new Timer("JSSServiceDestroyTimer", true);

  /**
   * Timer tick.
   */
  private long mTimerTick = 0;

  private Credential mCredential = null;

  private ClientConfig mClientConf = null;

  public JSSServiceOperator(Configuration conf) {
    mCredential =
        new Credential(conf.get(Constants.JSS_ACCESS_KEY), conf.get(Constants.JSS_SECRET_KEY));
    mClientConf = new ClientConfig();
    mClientConf.setConnectionTimeout(conf.getInt(Constants.UNDERFS_JSS_CONNECT_TIMEOUT));
    mClientConf.setEndpoint(conf.get(Constants.JSS_ENDPOINT));
    mClientConf.setMaxConnections(conf.getInt(Constants.UNDERFS_JSS_CONNECT_MAX));
    mClientConf.setSocketTimeout(conf.getInt(Constants.UNDERFS_JSS_SOCKET_TIMEOUT));
    mTimerTick = conf.getLong(Constants.UNDERFS_JSS_CACHE_TTL);
  }

  /**
   * If refs from 0 to 1 and mJSSClient has been destroy(is null), create a new instance.
   */
  @Override
  public void addRef() {
    // if ref count is from 0 to 1
    if (1 == mJSSClientRefs.incrementAndGet()) {
      if (null == mJSSClient) {
        synchronized (mClientAliveLockObj) {
          if (null == mJSSClient) {
            mJSSClient = new JingdongStorageService(mCredential, mClientConf);
            LOG.debug("Create a new JSSClient.");
          }
        }
      } else {
        // if ref is not 0
        mTimer.cancel();
      }
    }
    LOG.debug("Current JSSClient refs count is {}.", mJSSClientRefs.get());
  }

  /**
   * If refs count is 0 and the destroy timer's tick is 0, destroy mJSSClient right now, but if
   * timer's tick isn't 0, we should wait a ttl duration, after the ttl, if the refs count is 0,
   * destroy mJSSClient.
   */
  @Override
  public void release() {
    if (0 == mJSSClientRefs.decrementAndGet()) {
      if (0 == mTimerTick) {
        synchronized (mClientAliveLockObj) {
          if (0 == mJSSClientRefs.get()) {
            // Because JSSClient connection is keep-alive, so we need release it manually.
            mJSSClient.destroy();
            mJSSClient = null;
          }
        }
      } else {
        // cancel other object's timer.
        mTimer.cancel();
        mTimer.schedule(new java.util.TimerTask() {
          @Override
          public void run() {
            mTimer.cancel();
            synchronized (mClientAliveLockObj) {
              if (0 == mJSSClientRefs.get()) {
                // Because JSSClient connection is keep-alive, so we need release it manually.
                mJSSClient.destroy();
                mJSSClient = null;
              }
            }
          }
        }, 0L, mTimerTick);
      }
    }
  }

  public JingdongStorageService getJSSClient() {
    return mJSSClient;
  }
}
