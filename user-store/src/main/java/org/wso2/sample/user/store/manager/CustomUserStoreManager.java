/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.sample.user.store.manager;

import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.claim.ClaimManager;
import org.wso2.carbon.user.core.jdbc.UniqueIDJDBCUserStoreManager;
import org.wso2.carbon.user.core.profile.ProfileConfigurationManager;

import java.util.Date;
import java.util.Map;

public class CustomUserStoreManager extends UniqueIDJDBCUserStoreManager {

    public CustomUserStoreManager() {

    }

    public CustomUserStoreManager(org.wso2.carbon.user.api.RealmConfiguration realmConfig,
                                  Map<String, Object> properties,
                                  ClaimManager claimManager,
                                  ProfileConfigurationManager profileManager,
                                  UserRealm realm, Integer tenantId)
            throws UserStoreException {

        super(realmConfig, properties, claimManager, profileManager, realm, tenantId, false);
    }

    @Override
    protected boolean authenticate(String userName, Object credential, boolean domainProvided)
            throws UserStoreException {

        String regexStr = "^[0-9]{10}$";
        if (userName.matches(regexStr)) {
            String[] users = getUserList("http://wso2.org/claims/mobile", userName, "default");
            if (users == null) {
                return false;
            }

            if (users.length > 1) {
                return false;
            }
            return super.authenticate(users[0], credential, domainProvided);

        }
        return super.authenticate(userName, credential, domainProvided);
    }

    public Date getPasswordExpirationTime(String userName) throws UserStoreException {

        return null;
    }
}
