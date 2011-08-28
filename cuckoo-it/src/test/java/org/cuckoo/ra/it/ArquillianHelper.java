/*
 * Copyright (C) 2011 akquinet tech@spree GmbH
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
package org.cuckoo.ra.it;

import org.cuckoo.ra.cci.CuckooConnection;
import org.cuckoo.ra.common.ConnectionMetaDataImpl;
import org.cuckoo.ra.jco.CuckooDestinationDataProvider;
import org.cuckoo.ra.spi.CuckooResourceAdapter;
import org.cuckoo.ra.util.ForwardingList;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.ResourceAdapterArchive;

public class ArquillianHelper
{
    static final String DEPLOYMENT_NAME = "CuckooTestRA";

    static EnterpriseArchive createEar( JavaArchive testJar )
    {
        JavaArchive jar = ShrinkWrap.create( JavaArchive.class, DEPLOYMENT_NAME + ".jar" );
        jar.addPackages( false, CuckooResourceAdapter.class.getPackage(), CuckooConnection.class.getPackage(),
                ForwardingList.class.getPackage(), CuckooDestinationDataProvider.class.getPackage(),
                ConnectionMetaDataImpl.class.getPackage() );

        ResourceAdapterArchive rar =
                ShrinkWrap.create( ResourceAdapterArchive.class, DEPLOYMENT_NAME + ".rar" );
        rar.addLibrary( jar );
        rar.addManifestResource( SecurityTest.class.getResource( "/META-INF/ra.xml" ), "ra.xml" );

        // must be test.ear, otherwise Arquillian does not guess the correct JNDI-name for the EJBs
        EnterpriseArchive ear = ShrinkWrap.create( EnterpriseArchive.class, "test.ear" );
        ear.addModule( "jboss5/cuckoo-secure-jboss-ds.xml", "cuckoo-jboss-ds.xml" );
        ear.addApplicationResource( "jboss5/jboss-app.xml", "jboss-app.xml" );
        ear.addModule( rar );
        ear.addModule( testJar );

        return ear;
    }
}
