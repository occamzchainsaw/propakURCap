package com.bi.propak.pickups;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class PickupProgramNodeService implements SwingProgramNodeService<PickupProgramNodeContribution, PickupProgramNodeView>{
	
	@Override
	public String getId() {
		return "pickupNode";
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
			return "Pozycje Pobrań";
		} else {
			return "Pickup Positions";
		}
	}

	@Override
	public PickupProgramNodeView createView(ViewAPIProvider apiProvider) {
		return new PickupProgramNodeView(apiProvider);
	}

	@Override
	public PickupProgramNodeContribution createNode(ProgramAPIProvider apiProvider, PickupProgramNodeView view,
			DataModel model, CreationContext context) {
		return new PickupProgramNodeContribution(apiProvider, model, view, context);
	}

}
