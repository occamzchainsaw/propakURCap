package com.bi.propak.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.bi.propak.pickups.PickupProgramNodeService;
import com.bi.propak.separator.SeparatorProgramNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;

/**
 * Hello world activator for the OSGi bundle URCAPS contribution
 *
 */
public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Activator for Main ProPak Node Starting");
		bundleContext.registerService(SwingProgramNodeService.class, new ProPakMainProgramNodeService(), null);
		bundleContext.registerService(SwingProgramNodeService.class, new PickupProgramNodeService(), null);
		bundleContext.registerService(SwingProgramNodeService.class, new SeparatorProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		//System.out.println("Activator says Goodbye World!");
	}
}