package com.tvd12.ezyfoxserver.support.test.controller.app;

import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyClientRequestListener;
import com.tvd12.ezyfox.core.exception.EzyBadRequestException;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;
import com.tvd12.ezyfoxserver.support.test.controller.Hello;

@EzySingleton
@EzyClientRequestListener("badRequestNotSend")
public class AppBadRequestNotSendRequestHandler 
		implements EzyUserRequestHandler<EzyAppContext, Hello> {

	@Override
	public void handle(EzyAppContext context, EzyUserSessionEvent event, Hello data) {
		throw new EzyBadRequestException(1, "server maintain", false);
	}

	@Override
	public Hello newData() {
		return new Hello();
	}

}
