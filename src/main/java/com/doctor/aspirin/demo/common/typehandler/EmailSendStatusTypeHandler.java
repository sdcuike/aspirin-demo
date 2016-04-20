package com.doctor.aspirin.demo.common.typehandler;

import org.apache.ibatis.type.MappedTypes;

import com.doctor.aspirin.demo.common.EmailSendStatus;

/**
 * 
 * @author sdcuike
 *
 *         Create At 2016年4月7日 上午11:09:44
 * 
 * 
 */
@MappedTypes(EmailSendStatus.class)
public class EmailSendStatusTypeHandler extends AbstractIEnumDescriptionHandler {

}
