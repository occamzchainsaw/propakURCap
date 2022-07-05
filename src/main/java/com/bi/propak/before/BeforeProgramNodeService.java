package com.bi.propak.before;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class BeforeProgramNodeService implements SwingProgramNodeService<BeforeProgramNodeContribution, BeforeProgramNodeView>{
	
	@Override
	public String getId() {
		return "beforeNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		// TODO Auto-generated method stub
		configuration.setChildrenAllowed(false);
		configuration.setUserInsertable(false);
	}

	@Override
	public String getTitle(Locale locale) {
		if("pl".equals(Locale.getDefault().getLanguage())) {
			return "Przed Pobraniem";
		} else {
			return "Before Pickup";
		}
	}

	@Override
	public BeforeProgramNodeView createView(ViewAPIProvider apiProvider) {
		return new BeforeProgramNodeView(apiProvider);
	}

	@Override
	public BeforeProgramNodeContribution createNode(ProgramAPIProvider apiProvider, BeforeProgramNodeView view,
			DataModel model, CreationContext context) {
		return new BeforeProgramNodeContribution(apiProvider, model, view, context);
	}

}
