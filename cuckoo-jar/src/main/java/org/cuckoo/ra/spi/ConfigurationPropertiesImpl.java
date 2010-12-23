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
package org.cuckoo.ra.spi;

@SuppressWarnings( "unused" )
public class ConfigurationPropertiesImpl implements ConfigurationProperties
{
    private String destinationName;
    private String jcoClient;
    private String jcoUser;
    private String jcoAliasUser;
    private String jcoPassword;
    private String jcoLanguage;
    private String jcoSapRouter;
    private String jcoSystemNumber;
    private String jcoApplicationServerHost;
    private String jcoMessageServerHost;
    private String jcoMessageServerPort;
    private String jcoR3Name;
    private String jcoGroup;
    private String jcoTrace;
    private String jcoCpicTrace;
    private String jcoUseSapgui;
    private String jcoCodepage;
    private String jcoSncMode;
    private String jcoSncPartnername;
    private String jcoSncQop;
    private String jcoSncMyName;
    private String jcoSncLibrary;
    private String jcoPeakLimit;
    private String jcoPoolCapacity;
    private String jcoExpirationTime;
    private String jcoExpirationCheckPeriod;
    private String jcoMaxGetClientTime;
    private String jcoRepositoryDestination;
    private String jcoRepositoryUser;
    private String jcoRepositoryPassword;
    private String jcoRepositorySncMode;

    /**
     * Copy constructor
     *
     * @param properties The ConfigurationProperties instance to copy
     */
    ConfigurationPropertiesImpl( ConfigurationProperties properties )
    {
        destinationName = properties.getDestinationName();
        jcoAliasUser = properties.getJcoAliasUser();
        jcoApplicationServerHost = properties.getJcoApplicationServerHost();
        jcoClient = properties.getJcoClient();
        jcoCodepage = properties.getJcoCodepage();
        jcoCpicTrace = properties.getJcoCpicTrace();
        jcoExpirationCheckPeriod = properties.getJcoExpirationCheckPeriod();
        jcoExpirationTime = properties.getJcoExpirationTime();
        jcoGroup = properties.getJcoGroup();
        jcoLanguage = properties.getJcoLanguage();
        jcoMaxGetClientTime = properties.getJcoMaxGetClientTime();
        jcoMessageServerHost = properties.getJcoMessageServerHost();
        jcoMessageServerPort = properties.getJcoMessageServerPort();
        jcoPassword = properties.getJcoPassword();
        jcoPeakLimit = properties.getJcoPeakLimit();
        jcoPoolCapacity = properties.getJcoPoolCapacity();
        jcoR3Name = properties.getJcoR3Name();
        jcoRepositoryDestination = properties.getJcoRepositoryDestination();
        jcoRepositoryPassword = properties.getJcoRepositoryPassword();
        jcoRepositorySncMode = properties.getJcoRepositorySncMode();
        jcoRepositoryUser = properties.getJcoRepositoryUser();
        jcoSapRouter = properties.getJcoSapRouter();
        jcoSncLibrary = properties.getJcoSncLibrary();
        jcoSncMode = properties.getJcoSncMode();
        jcoSncMyName = properties.getJcoSncMyName();
        jcoSncPartnername = properties.getJcoSncPartnername();
        jcoSncQop = properties.getJcoSncQop();
        jcoSystemNumber = properties.getJcoSystemNumber();
        jcoTrace = properties.getJcoTrace();
        jcoUser = properties.getJcoUser();
        jcoUseSapgui = properties.getJcoUseSapgui();
    }

    ConfigurationPropertiesImpl()
    {
        // nothing to do
    }

    public String getDestinationName()
    {
        return destinationName;
    }

    public void setDestinationName( String destinationName )
    {
        this.destinationName = destinationName;
    }

    public String getJcoClient()
    {
        return jcoClient;
    }

    public void setJcoClient( String jcoClient )
    {
        this.jcoClient = jcoClient;
    }

    public String getJcoUser()
    {
        return jcoUser;
    }

    public void setJcoUser( String jcoUser )
    {
        this.jcoUser = jcoUser;
    }

    public String getJcoAliasUser()
    {
        return jcoAliasUser;
    }

    public void setJcoAliasUser( String jcoAliasUser )
    {
        this.jcoAliasUser = jcoAliasUser;
    }

    public String getJcoPassword()
    {
        return jcoPassword;
    }

    public void setJcoPassword( String jcoPassword )
    {
        this.jcoPassword = jcoPassword;
    }

    public String getJcoLanguage()
    {
        return jcoLanguage;
    }

    public void setJcoLanguage( String jcoLanguage )
    {
        this.jcoLanguage = jcoLanguage;
    }

    public String getJcoSapRouter()
    {
        return jcoSapRouter;
    }

    public void setJcoSapRouter( String jcoSapRouter )
    {
        this.jcoSapRouter = jcoSapRouter;
    }

    public String getJcoSystemNumber()
    {
        return jcoSystemNumber;
    }

    public void setJcoSystemNumber( String jcoSystemNumber )
    {
        this.jcoSystemNumber = jcoSystemNumber;
    }

    public String getJcoApplicationServerHost()
    {
        return jcoApplicationServerHost;
    }

    public void setJcoApplicationServerHost( String jcoApplicationServerHost )
    {
        this.jcoApplicationServerHost = jcoApplicationServerHost;
    }

    public String getJcoMessageServerHost()
    {
        return jcoMessageServerHost;
    }

    public void setJcoMessageServerHost( String jcoMessageServerHost )
    {
        this.jcoMessageServerHost = jcoMessageServerHost;
    }

    public String getJcoMessageServerPort()
    {
        return jcoMessageServerPort;
    }

    public void setJcoMessageServerPort( String jcoMessageServerPort )
    {
        this.jcoMessageServerPort = jcoMessageServerPort;
    }

    public String getJcoR3Name()
    {
        return jcoR3Name;
    }

    public void setJcoR3Name( String jcoR3Name )
    {
        this.jcoR3Name = jcoR3Name;
    }

    public String getJcoGroup()
    {
        return jcoGroup;
    }

    public void setJcoGroup( String jcoGroup )
    {
        this.jcoGroup = jcoGroup;
    }

    public String getJcoTrace()
    {
        return jcoTrace;
    }

    public void setJcoTrace( String jcoTrace )
    {
        this.jcoTrace = jcoTrace;
    }

    public String getJcoCpicTrace()
    {
        return jcoCpicTrace;
    }

    public void setJcoCpicTrace( String jcoCpicTrace )
    {
        this.jcoCpicTrace = jcoCpicTrace;
    }

    public String getJcoUseSapgui()
    {
        return jcoUseSapgui;
    }

    public void setJcoUseSapgui( String jcoUseSapgui )
    {
        this.jcoUseSapgui = jcoUseSapgui;
    }

    public String getJcoCodepage()
    {
        return jcoCodepage;
    }

    public void setJcoCodepage( String jcoCodepage )
    {
        this.jcoCodepage = jcoCodepage;
    }

    public String getJcoSncMode()
    {
        return jcoSncMode;
    }

    public void setJcoSncMode( String jcoSncMode )
    {
        this.jcoSncMode = jcoSncMode;
    }

    public String getJcoSncPartnername()
    {
        return jcoSncPartnername;
    }

    public void setJcoSncPartnername( String jcoSncPartnername )
    {
        this.jcoSncPartnername = jcoSncPartnername;
    }

    public String getJcoSncQop()
    {
        return jcoSncQop;
    }

    public void setJcoSncQop( String jcoSncQop )
    {
        this.jcoSncQop = jcoSncQop;
    }

    public String getJcoSncMyName()
    {
        return jcoSncMyName;
    }

    public void setJcoSncMyName( String jcoSncMyName )
    {
        this.jcoSncMyName = jcoSncMyName;
    }

    public String getJcoSncLibrary()
    {
        return jcoSncLibrary;
    }

    public void setJcoSncLibrary( String jcoSncLibrary )
    {
        this.jcoSncLibrary = jcoSncLibrary;
    }

    public String getJcoPeakLimit()
    {
        return jcoPeakLimit;
    }

    public void setJcoPeakLimit( String jcoPeakLimit )
    {
        this.jcoPeakLimit = jcoPeakLimit;
    }

    public String getJcoPoolCapacity()
    {
        return jcoPoolCapacity;
    }

    public void setJcoPoolCapacity( String jcoPoolCapacity )
    {
        this.jcoPoolCapacity = jcoPoolCapacity;
    }

    public String getJcoExpirationTime()
    {
        return jcoExpirationTime;
    }

    public void setJcoExpirationTime( String jcoExpirationTime )
    {
        this.jcoExpirationTime = jcoExpirationTime;
    }

    public String getJcoExpirationCheckPeriod()
    {
        return jcoExpirationCheckPeriod;
    }

    public void setJcoExpirationCheckPeriod( String jcoExpirationCheckPeriod )
    {
        this.jcoExpirationCheckPeriod = jcoExpirationCheckPeriod;
    }

    public String getJcoMaxGetClientTime()
    {
        return jcoMaxGetClientTime;
    }

    public void setJcoMaxGetClientTime( String jcoMaxGetClientTime )
    {
        this.jcoMaxGetClientTime = jcoMaxGetClientTime;
    }

    public String getJcoRepositoryDestination()
    {
        return jcoRepositoryDestination;
    }

    public void setJcoRepositoryDestination( String jcoRepositoryDestination )
    {
        this.jcoRepositoryDestination = jcoRepositoryDestination;
    }

    public String getJcoRepositoryUser()
    {
        return jcoRepositoryUser;
    }

    public void setJcoRepositoryUser( String jcoRepositoryUser )
    {
        this.jcoRepositoryUser = jcoRepositoryUser;
    }

    public String getJcoRepositoryPassword()
    {
        return jcoRepositoryPassword;
    }

    public void setJcoRepositoryPassword( String jcoRepositoryPassword )
    {
        this.jcoRepositoryPassword = jcoRepositoryPassword;
    }

    public String getJcoRepositorySncMode()
    {
        return jcoRepositorySncMode;
    }

    public void setJcoRepositorySncMode( String jcoRepositorySncMode )
    {
        this.jcoRepositorySncMode = jcoRepositorySncMode;
    }

    @Override
    public int hashCode()
    {
        int result = jcoClient != null ? jcoClient.hashCode() : 0;
        result = 31 * result + ( destinationName != null ? destinationName.hashCode() : 0 );
        result = 31 * result + ( jcoUser != null ? jcoUser.hashCode() : 0 );
        result = 31 * result + ( jcoAliasUser != null ? jcoAliasUser.hashCode() : 0 );
        result = 31 * result + ( jcoPassword != null ? jcoPassword.hashCode() : 0 );
        result = 31 * result + ( jcoLanguage != null ? jcoLanguage.hashCode() : 0 );
        result = 31 * result + ( jcoSapRouter != null ? jcoSapRouter.hashCode() : 0 );
        result = 31 * result + ( jcoSystemNumber != null ? jcoSystemNumber.hashCode() : 0 );
        result = 31 * result + ( jcoApplicationServerHost != null ? jcoApplicationServerHost.hashCode() : 0 );
        result = 31 * result + ( jcoMessageServerHost != null ? jcoMessageServerHost.hashCode() : 0 );
        result = 31 * result + ( jcoMessageServerPort != null ? jcoMessageServerPort.hashCode() : 0 );
        result = 31 * result + ( jcoR3Name != null ? jcoR3Name.hashCode() : 0 );
        result = 31 * result + ( jcoGroup != null ? jcoGroup.hashCode() : 0 );
        result = 31 * result + ( jcoTrace != null ? jcoTrace.hashCode() : 0 );
        result = 31 * result + ( jcoCpicTrace != null ? jcoCpicTrace.hashCode() : 0 );
        result = 31 * result + ( jcoUseSapgui != null ? jcoUseSapgui.hashCode() : 0 );
        result = 31 * result + ( jcoCodepage != null ? jcoCodepage.hashCode() : 0 );
        result = 31 * result + ( jcoSncMode != null ? jcoSncMode.hashCode() : 0 );
        result = 31 * result + ( jcoSncPartnername != null ? jcoSncPartnername.hashCode() : 0 );
        result = 31 * result + ( jcoSncQop != null ? jcoSncQop.hashCode() : 0 );
        result = 31 * result + ( jcoSncMyName != null ? jcoSncMyName.hashCode() : 0 );
        result = 31 * result + ( jcoSncLibrary != null ? jcoSncLibrary.hashCode() : 0 );
        result = 31 * result + ( jcoPeakLimit != null ? jcoPeakLimit.hashCode() : 0 );
        result = 31 * result + ( jcoPoolCapacity != null ? jcoPoolCapacity.hashCode() : 0 );
        result = 31 * result + ( jcoExpirationTime != null ? jcoExpirationTime.hashCode() : 0 );
        result = 31 * result + ( jcoExpirationCheckPeriod != null ? jcoExpirationCheckPeriod.hashCode() : 0 );
        result = 31 * result + ( jcoMaxGetClientTime != null ? jcoMaxGetClientTime.hashCode() : 0 );
        result = 31 * result + ( jcoRepositoryDestination != null ? jcoRepositoryDestination.hashCode() : 0 );
        result = 31 * result + ( jcoRepositoryUser != null ? jcoRepositoryUser.hashCode() : 0 );
        result = 31 * result + ( jcoRepositoryPassword != null ? jcoRepositoryPassword.hashCode() : 0 );
        result = 31 * result + ( jcoRepositorySncMode != null ? jcoRepositorySncMode.hashCode() : 0 );
        return result;
    }

    @SuppressWarnings( {"RedundantIfStatement"} )
    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        ConfigurationPropertiesImpl that = ( ConfigurationPropertiesImpl ) o;

        if ( destinationName != null ? !destinationName.equals( that.destinationName ) : that.destinationName != null )
        {
            return false;
        }
        if ( jcoAliasUser != null ? !jcoAliasUser.equals( that.jcoAliasUser ) : that.jcoAliasUser != null )
        {
            return false;
        }
        if ( jcoApplicationServerHost != null ? !jcoApplicationServerHost.equals( that.jcoApplicationServerHost ) :
             that.jcoApplicationServerHost != null )
        {
            return false;
        }
        if ( jcoClient != null ? !jcoClient.equals( that.jcoClient ) : that.jcoClient != null )
        {
            return false;
        }
        if ( jcoCodepage != null ? !jcoCodepage.equals( that.jcoCodepage ) : that.jcoCodepage != null )
        {
            return false;
        }
        if ( jcoCpicTrace != null ? !jcoCpicTrace.equals( that.jcoCpicTrace ) : that.jcoCpicTrace != null )
        {
            return false;
        }
        if ( jcoGroup != null ? !jcoGroup.equals( that.jcoGroup ) : that.jcoGroup != null )
        {
            return false;
        }
        if ( jcoLanguage != null ? !jcoLanguage.equals( that.jcoLanguage ) : that.jcoLanguage != null )
        {
            return false;
        }
        if ( jcoMessageServerHost != null ? !jcoMessageServerHost.equals( that.jcoMessageServerHost ) :
             that.jcoMessageServerHost != null )
        {
            return false;
        }
        if ( jcoMessageServerPort != null ? !jcoMessageServerPort.equals( that.jcoMessageServerPort ) :
             that.jcoMessageServerPort != null )
        {
            return false;
        }
        if ( jcoPassword != null ? !jcoPassword.equals( that.jcoPassword ) : that.jcoPassword != null )
        {
            return false;
        }
        if ( jcoR3Name != null ? !jcoR3Name.equals( that.jcoR3Name ) : that.jcoR3Name != null )
        {
            return false;
        }
        if ( jcoSapRouter != null ? !jcoSapRouter.equals( that.jcoSapRouter ) : that.jcoSapRouter != null )
        {
            return false;
        }
        if ( jcoSncLibrary != null ? !jcoSncLibrary.equals( that.jcoSncLibrary ) : that.jcoSncLibrary != null )
        {
            return false;
        }
        if ( jcoSncMode != null ? !jcoSncMode.equals( that.jcoSncMode ) : that.jcoSncMode != null )
        {
            return false;
        }
        if ( jcoSncMyName != null ? !jcoSncMyName.equals( that.jcoSncMyName ) : that.jcoSncMyName != null )
        {
            return false;
        }
        if ( jcoSncPartnername != null ? !jcoSncPartnername.equals( that.jcoSncPartnername ) :
             that.jcoSncPartnername != null )
        {
            return false;
        }
        if ( jcoSncQop != null ? !jcoSncQop.equals( that.jcoSncQop ) : that.jcoSncQop != null )
        {
            return false;
        }
        if ( jcoSystemNumber != null ? !jcoSystemNumber.equals( that.jcoSystemNumber ) : that.jcoSystemNumber != null )
        {
            return false;
        }
        if ( jcoTrace != null ? !jcoTrace.equals( that.jcoTrace ) : that.jcoTrace != null )
        {
            return false;
        }
        if ( jcoUseSapgui != null ? !jcoUseSapgui.equals( that.jcoUseSapgui ) : that.jcoUseSapgui != null )
        {
            return false;
        }
        if ( jcoUser != null ? !jcoUser.equals( that.jcoUser ) : that.jcoUser != null )
        {
            return false;
        }
        if ( jcoExpirationCheckPeriod != null ? !jcoExpirationCheckPeriod.equals( that.jcoExpirationCheckPeriod ) :
             that.jcoExpirationCheckPeriod != null )
        {
            return false;
        }
        if ( jcoExpirationTime != null ? !jcoExpirationTime.equals( that.jcoExpirationTime ) :
             that.jcoExpirationTime != null )
        {
            return false;
        }
        if ( jcoMaxGetClientTime != null ? !jcoMaxGetClientTime.equals( that.jcoMaxGetClientTime ) :
             that.jcoMaxGetClientTime != null )
        {
            return false;
        }
        if ( jcoPeakLimit != null ? !jcoPeakLimit.equals( that.jcoPeakLimit ) : that.jcoPeakLimit != null )
        {
            return false;
        }
        if ( jcoPoolCapacity != null ? !jcoPoolCapacity.equals( that.jcoPoolCapacity ) : that.jcoPoolCapacity != null )
        {
            return false;
        }
        if ( jcoRepositoryDestination != null ? !jcoRepositoryDestination.equals( that.jcoRepositoryDestination ) :
             that.jcoRepositoryDestination != null )
        {
            return false;
        }
        if ( jcoRepositoryPassword != null ? !jcoRepositoryPassword.equals( that.jcoRepositoryPassword ) :
             that.jcoRepositoryPassword != null )
        {
            return false;
        }
        if ( jcoRepositorySncMode != null ? !jcoRepositorySncMode.equals( that.jcoRepositorySncMode ) :
             that.jcoRepositorySncMode != null )
        {
            return false;
        }
        if ( jcoRepositoryUser != null ? !jcoRepositoryUser.equals( that.jcoRepositoryUser ) :
             that.jcoRepositoryUser != null )
        {
            return false;
        }
        return true;
    }
}
