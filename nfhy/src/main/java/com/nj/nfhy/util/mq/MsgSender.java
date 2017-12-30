package com.nj.nfhy.util.mq;

import com.nj.nfhy.util.basicUtils.DateUtil;
import jxl.common.Logger;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;
import java.util.Date;

public class MsgSender {
	Logger log = Logger.getLogger(MsgSender.class);
	private final JmsTemplate jmsTemplate;
	private final Destination destination;

	public MsgSender(final JmsTemplate jmsTemplate, final Destination destination) {
		this.jmsTemplate = jmsTemplate;
		this.destination = destination;
	}

	public void send(String text,long delay) {
		try {
			jmsTemplate.setDefaultDestination(destination);
			ScheduleMessagePostProcessor scheduleMessagePostProcessor = new ScheduleMessagePostProcessor(delay);
			jmsTemplate.convertAndSend((Object)text, scheduleMessagePostProcessor);
			log.info(MsgSender.class+"====发送内容==>"+text);
			log.info(MsgSender.class+"====发送时间==>"+ DateUtil.time2Str(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
