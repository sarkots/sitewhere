/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.groovy.device.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;

import com.sitewhere.SiteWhere;
import com.sitewhere.server.lifecycle.TenantLifecycleComponent;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.device.communication.EventDecodeException;
import com.sitewhere.spi.device.communication.IDecodedDeviceRequest;
import com.sitewhere.spi.device.communication.IDeviceEventDecoder;
import com.sitewhere.spi.server.lifecycle.ILifecycleProgressMonitor;
import com.sitewhere.spi.server.lifecycle.LifecycleComponentType;

import groovy.lang.Binding;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

/**
 * Implementation of {@link IDeviceEventDecoder} that uses a Groovy script to
 * decode a binary payload.
 * 
 * @author Derek
 */
public class GroovyEventDecoder extends TenantLifecycleComponent implements IDeviceEventDecoder<byte[]> {

    /** Static logger instance */
    private static Logger LOGGER = LogManager.getLogger();

    /** Path to script used for decoder */
    private String scriptPath;

    public GroovyEventDecoder() {
	super(LifecycleComponentType.DeviceEventDecoder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sitewhere.spi.device.communication.IDeviceEventDecoder#decode(java.
     * lang.Object, java.util.Map)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<IDecodedDeviceRequest<?>> decode(byte[] payload, Map<String, Object> metadata)
	    throws EventDecodeException {
	try {
	    Binding binding = new Binding();
	    List<IDecodedDeviceRequest<?>> events = new ArrayList<IDecodedDeviceRequest<?>>();
	    binding.setVariable(IGroovyVariables.VAR_DECODED_EVENTS, events);
	    binding.setVariable(IGroovyVariables.VAR_PAYLOAD, payload);
	    binding.setVariable(IGroovyVariables.VAR_PAYLOAD_METADATA, metadata);
	    binding.setVariable(IGroovyVariables.VAR_LOGGER, LOGGER);
	    LOGGER.debug("About to execute '" + getScriptPath() + "' with payload: " + payload);
	    SiteWhere.getServer().getTenantGroovyConfiguration(getTenant()).getGroovyScriptEngine().run(getScriptPath(),
		    binding);
	    return (List<IDecodedDeviceRequest<?>>) binding.getVariable(IGroovyVariables.VAR_DECODED_EVENTS);
	} catch (ResourceException e) {
	    throw new EventDecodeException("Unable to access Groovy decoder script.", e);
	} catch (ScriptException e) {
	    throw new EventDecodeException("Unable to run Groovy decoder script.", e);
	} catch (CompilationFailedException e) {
	    throw new EventDecodeException("Error compiling Groovy script.", e);
	} catch (Throwable e) {
	    throw new EventDecodeException("Unhandled exception in Groovy decoder script.", e);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#start(com.
     * sitewhere.spi.server.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void start(ILifecycleProgressMonitor monitor) throws SiteWhereException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sitewhere.spi.server.lifecycle.ILifecycleComponent#stop(com.sitewhere
     * .spi.server.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void stop(ILifecycleProgressMonitor monitor) throws SiteWhereException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#getLogger()
     */
    @Override
    public Logger getLogger() {
	return LOGGER;
    }

    public String getScriptPath() {
	return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
	this.scriptPath = scriptPath;
    }
}