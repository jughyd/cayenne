/*****************************************************************
 *   Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 ****************************************************************/
package org.apache.cayenne.di.spi;

import org.apache.cayenne.ConfigurationException;
import org.apache.cayenne.di.Key;
import org.apache.cayenne.di.MapBuilder;

/**
 * @since 3.1
 */
class DefaultMapBuilder<T> implements MapBuilder<T> {

    private DefaultInjector injector;
    private Key<T> implementationTypeKey;

    DefaultMapBuilder(Class<T> implementationType, DefaultInjector injector) {
        this.injector = injector;
        implementationTypeKey = Key.get(implementationType);
    }

    public <E> MapBuilder<T> put(String key, Class<? extends E> interfaceType)
            throws ConfigurationException {

        MapProvider mapProvider = injector.getMapConfigurations().get(
                implementationTypeKey);
        if (mapProvider == null) {
            mapProvider = new MapProvider();
            injector.getMapConfigurations().put(implementationTypeKey, mapProvider);
        }

        // TODO: andrus 11/15/2009 - report overriding the key??
        mapProvider.put(key, injector.getProvider(interfaceType));
        return this;
    }

    public <E> MapBuilder<T> put(String key, E value) throws ConfigurationException {

        InstanceProvider<E> provider = new InstanceProvider<E>(value);

        MapProvider mapProvider = injector.getMapConfigurations().get(
                implementationTypeKey);
        if (mapProvider == null) {
            mapProvider = new MapProvider();
            injector.getMapConfigurations().put(implementationTypeKey, mapProvider);
        }

        // TODO: andrus 11/15/2009 - report overriding the key??
        mapProvider.put(key, provider);
        return this;
    }
}
