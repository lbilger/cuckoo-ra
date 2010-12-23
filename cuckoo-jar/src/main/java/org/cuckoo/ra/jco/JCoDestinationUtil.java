/*
 * Copyright (C) 2010 akquinet tech@spree GmbH
 *
 * This file is part of the Cuckoo Resource Adapter for SAP.
 *
 * Cuckoo Resource Adapter for SAP is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Cuckoo Resource Adapter for SAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with Cuckoo Resource Adapter for SAP. If not, see <http://www.gnu.org/licenses/>.
 */
package org.cuckoo.ra.jco;

import com.sap.conn.jco.ext.DestinationDataProvider;
import org.cuckoo.ra.spi.ConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class JCoDestinationUtil
{
    private static final Logger LOG = LoggerFactory.getLogger(JCoDestinationUtil.class);

    private JCoDestinationUtil()
    {
        // use static methods
    }

    public static Properties createJCoDestinationProperties(ConfigurationProperties config)
    {
        LOG.trace("ConfigurationPropertiesImpl.createJCoDestinationProperties()");

        final Properties properties = new Properties();
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_CLIENT, config.getJcoClient());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_USER, config.getJcoUser());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_ALIAS_USER, config.getJcoAliasUser());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_PASSWD, config.getJcoPassword());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_LANG, config.getJcoLanguage());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_SAPROUTER, config.getJcoSapRouter());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_SYSNR, config.getJcoSystemNumber());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_ASHOST, config.getJcoApplicationServerHost());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_MSHOST, config.getJcoMessageServerHost());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_MSSERV, config.getJcoMessageServerPort());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_R3NAME, config.getJcoR3Name());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_GROUP, config.getJcoGroup());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_TRACE, config.getJcoTrace());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_CPIC_TRACE, config.getJcoCpicTrace());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_USE_SAPGUI, config.getJcoUseSapgui());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_CODEPAGE, config.getJcoCodepage());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_SNC_MODE, config.getJcoSncMode());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_SNC_PARTNERNAME, config.getJcoSncPartnername());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_SNC_QOP, config.getJcoSncQop());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_SNC_MYNAME, config.getJcoSncMyName());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_SNC_LIBRARY, config.getJcoSncLibrary());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_PEAK_LIMIT, config.getJcoPeakLimit());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_POOL_CAPACITY, config.getJcoPoolCapacity());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_EXPIRATION_TIME, config.getJcoExpirationTime());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_EXPIRATION_PERIOD, config.getJcoExpirationCheckPeriod());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_MAX_GET_TIME, config.getJcoMaxGetClientTime());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_REPOSITORY_DEST, config.getJcoRepositoryDestination());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_REPOSITORY_USER, config.getJcoRepositoryUser());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_REPOSITORY_PASSWD, config.getJcoRepositoryPassword());
        addPropertyNullSafe(properties, DestinationDataProvider.JCO_REPOSITORY_SNC, config.getJcoRepositorySncMode());
        return properties;
    }

    private static void addPropertyNullSafe(Properties properties, String name, String value)
    {
        if (value != null)
        {
            properties.setProperty(name, value);
        }
    }

}
