/**
 * 
 */
package com.nj.nfhy.util.mq;

import com.nj.nfhy.util.basicUtils.ValidateUtil;
import org.apache.activemq.ScheduledMessage;
import org.springframework.jms.core.MessagePostProcessor;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * @author liujun 2016年11月22日
 *
 */
public class ScheduleMessagePostProcessor implements MessagePostProcessor{
	 private long delay = 0l;
	 
	 private String corn = null;
	 
	public ScheduleMessagePostProcessor(long delay) {
	        this.delay = delay;
	    }
	 
	    public ScheduleMessagePostProcessor(String cron) {
	        this.corn = cron;
	    }
	 
	    public Message postProcessMessage(Message message) throws JMSException {
	        if (delay > 0) {
	            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
	        }
	        if (ValidateUtil.isNotEmpty(corn)) {
	            message.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, corn);
	        }
	        return message;
	    }
}
