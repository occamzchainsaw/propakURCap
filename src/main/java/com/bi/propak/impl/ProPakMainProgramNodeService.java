package com.bi.propak.impl;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class ProPakMainProgramNodeService implements SwingProgramNodeService<ProPakMainProgramNodeContribution, ProPakMainProgramNodeView>{

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return "mainProPakNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		// TODO Auto-generated method stub
		configuration.setChildrenAllowed(true);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Program";
	}

	@Override
	public ProPakMainProgramNodeView createView(ViewAPIProvider apiProvider) {
		// TODO Auto-generated method stub
		return new ProPakMainProgramNodeView(apiProvider);
	}

	@Override
	public ProPakMainProgramNodeContribution createNode(ProgramAPIProvider apiProvider, ProPakMainProgramNodeView view,
			DataModel model, CreationContext context) {
		// TODO Auto-generated method stub
		return new ProPakMainProgramNodeContribution(apiProvider, view, model, context);
	}

}
