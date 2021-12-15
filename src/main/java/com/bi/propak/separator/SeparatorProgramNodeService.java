package com.bi.propak.separator;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class SeparatorProgramNodeService implements SwingProgramNodeService<SeparatorProgramNodeContribution, SeparatorProgramNodeView>{

	@Override
	public String getId() {
		return "separatorNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);
		configuration.setUserInsertable(false);
	}

	@Override
	public String getTitle(Locale locale) {
		if("pl".equals(Locale.getDefault().getLanguage())) {
			return "Trajektoria Przekładki";
		} else {
			return "Separator Trajectory";
		}
	}

	@Override
	public SeparatorProgramNodeView createView(ViewAPIProvider apiProvider) {
		// TODO Auto-generated method stub
		return new SeparatorProgramNodeView(apiProvider);
	}

	@Override
	public SeparatorProgramNodeContribution createNode(ProgramAPIProvider apiProvider, SeparatorProgramNodeView view,
			DataModel model, CreationContext context) {
		return new SeparatorProgramNodeContribution(apiProvider, model, view, context);
	}
}
