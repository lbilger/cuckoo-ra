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
package org.cuckoo.ra.cci;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.resource.cci.ResourceAdapterMetaData;

public class CuckooRaMetaData implements ResourceAdapterMetaData
{
    private static final Logger LOG = LoggerFactory.getLogger(CuckooRaMetaData.class);

    public CuckooRaMetaData()
    {
        LOG.trace("CuckooRaMetaData()");
    }

    /**
     * Gets the version of the resource adapter.
     *
     * @return String representing version of the resource adapter
     */
    public String getAdapterVersion()
    {
        return "0.1"; // TODO get from ra.xml
    }

    /**
     * Gets the name of the vendor that has provided the resource adapter.
     *
     * @return String representing name of the vendor
     */
    public String getAdapterVendorName()
    {
        return "akquinet tech@spree GmbH"; // TODO get from ra.xml
    }

    /**
     * Gets a tool displayable name of the resource adapter.
     *
     * @return String representing the name of the resource adapter
     */
    public String getAdapterName()
    {
        return "Cuckoo Resource Adapter for SAP"; // TODO get from ra.xml
    }

    /**
     * Gets a tool displayable short desription of the resource adapter.
     *
     * @return String describing the resource adapter
     */
    public String getAdapterShortDescription()
    {
        return "Cuckoo RA"; // TODO get from ra.xml
    }

    /**
     * Returns a string representation of the version
     *
     * @return String representing the supported version of the connector architecture
     */
    public String getSpecVersion()
    {
        return "1.5"; // TODO get from ra.xml
    }

    /**
     * Returns an array of fully-qualified names of InteractionSpec
     *
     * @return Array of fully-qualified class names of InteractionSpec classes
     */
    public String[] getInteractionSpecsSupported()
    {
        return new String[]{CuckooInteractionSpec.class.getName()};
    }

    /**
     * Returns true if the implementation class for the Interaction
     *
     * @return boolean depending on method support
     */
    public boolean supportsExecuteWithInputAndOutputRecord()
    {
        return false;
    }

    /**
     * Returns true if the implementation class for the Interaction
     *
     * @return boolean depending on method support
     */
    public boolean supportsExecuteWithInputRecordOnly()
    {
        return true;
    }

    /**
     * Returns true if the resource adapter implements the LocalTransaction
     *
     * @return true if resource adapter supports resource manager local transaction demarcation
     */
    public boolean supportsLocalTransactionDemarcation()
    {
        return true;
    }


}
