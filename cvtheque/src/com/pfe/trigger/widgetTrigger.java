package com.pfe.trigger;


import com.exalead.cv360.customcomponents.CustomComponent;
import com.exalead.cv360.searchui.triggers.MashupWidgetTrigger;
import com.exalead.cv360.searchui.triggers.TriggerContext;
import com.exalead.cv360.searchui.widgets.DataWidgetWrapper;


import com.exalead.cv360.searchui.security.SecurityModel;

@CustomComponent(displayName = "custom widget trigger")
public class widgetTrigger implements MashupWidgetTrigger {
	
	
	
	@Override
	public void afterRendering(DataWidgetWrapper dww, TriggerContext triggerContext) {
		
	}

	@Override
	public boolean beforeRendering(DataWidgetWrapper dww, TriggerContext triggerContext) {
		SecurityModel smodel = (SecurityModel) triggerContext.getSession().getAttribute("security");
		if( !smodel.getLogin().equalsIgnoreCase("dsi") ) {
			dww.getWidget().setEnable(false);
		}
	
return true;
}
	
}
