package com.mpt.constantes

/**
 * Constantes generales utilizadas por el backend del MCE y FirmaEC
 * 
 * @author ajcm
 * @version 1.0
 */
class MPTConstants {

	static final String BASE_DOCUMENT_NAME= "UNL_FEIRNNR_CISC_PO"
	static final String DOCX_EXTENSION = ".docx"
	static final String PDF_EXTENSION = ".pdf"
	static final String REPLACEMENT_PDF_EXTENSION = "-signed.pdf"
	static final String MYME_TYPE_PDF = "application/pdf"
	static final String MYME_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"

	/**
	 * Corresponde al nombre del sistema en la BD de firmaEC
	 */
	static final String SYSTEM_NAME = "mce"

	/**
	 * Header HTTP con la clave para poder utilizar el servicio web de firmaEC
	 */
	static final String ADDITIONAL_HTTP_HEADER_KEY = "X-API-KEY"

	/**
	 * Servidor y puerto SMTP (Simple Mail Transfer Protocol)
	 */
	static final String SMTP_SERVER = "smtp.gmail.com"
	static final Integer SMTP_PORT = 465

	/**
	 * Indica si se usa cifrado SSL para el envío de correos electrónicos
	 */
	static final Boolean SMPT_SERVER_USE_SSL = true

	/**
	 * Nombre del campo Cédula de la sección Información personalizada de cada usuario
	 */
	static final String IDENTIFICATION_DOCUMENT_TYPE = "Cédula"
	/**
	 * Nombres de las tareas Firmar certificado
	 */
	static final String TASK_NAME_SIGN_CERTIFICATE_SECRETARY = "Firmar certificado S"
	static final String TASK_NAME_SIGN_CERTIFICATE_GESTOR= "Firmar certificado G"
	static final String TASK_NAME_SIGN_CERTIFICATE_DEAN = "Firmar certificado D"
	
	/**
	 * Duración en milisegundos del token de firma enviado por el servicio web de firma
	 */
	static final Long SIGNATURE_TOKEN_LIFETIME = 1209600000L // 2 semanas
	static final Long SIGNATURE_TOKEN_LIFETIME_PRE = 300000L // 5 minutos
	
	/**
	 * Duración en milisegundos del temporizador para autofinalizar tarea
	 */
	static final Long AUTOFINALIZE_TASK_TIMER = 1209600000L // 2 semanas
	static final Long AUTOFINALIZE_TASK_TIMER_PRE = 300000L // 5 minutos
	
	/**
	 * Iniciales de la carrera insertado en las plantillas de certificados
	 */
	static final String CAREER_INITIALS = "COM"
	
	/**
	 * Valores para formateo de fechas
	 */
	static final String DATE_PATTERN = "d 'de' MMMM 'de' yyyy"  // Ejm.: 20 de mayo de 2022
	static final String DATE_PATTERN_SHORT = "dd-MMMM-yyyy"  // Ejm.: 20-mayo-2022
	static final String LANGUAGE_CODE = "es"
	static final String COUNTRY_CODE = "EC"
	
	/**
	 * Nombre de opciones disponibles para el proceso de Crear o editar
	 */	
	static final String OPTION_CREATE_OR_EDIT_PARAM = "PARAMETRO"
	

	/**
	 * Datos de Alfresco server para almacenamiento de certificados académicos firmados
	 */
	static final String ALFRESCO_REPOSITORY_NAME = "Main Repository"
	static final String ALFRESCO_PARENT_FOLDER = "/UNL"
	static final String ALFRESCO_PARENT_FOLDER_PATH_INFORMES = "/Informes"
	static final String ALFRESCO_PARENT_FOLDER_PATH_SOLICITUDES = "/Solicitudes"

	/**
	 * Dirección web de Alfresco donde se almacenan los certificados firmados. Se debe anexar con el id retornado del
	 * documento guardado en Alfresco. Ejm:
	 * https://ciscunl.info/share/page/document-details?nodeRef=workspace://SpacesStore/5e9d6d9d-9aa5-410c-97ea-e6963d4aed89
	 * 
	 * Getting (Download)the contents of a specific document by id. Ejm:
	 * https://ciscunl.info/alfresco/api/-default-/public/cmis/versions/1.1/atom/content?id=5e9d6d9d-9aa5-410c-97ea-e6963d4aed89
	 */
	//// static final String ALFRESCO_DIRECTORY_DOCUMENTS = "https://ciscunl.info/share/page/document-details?nodeRef=workspace://SpacesStore/"

	/**
	 * Datos Alfresco server para almacenamiento de plantillas DOCX de certificados y solicitudes
	 * para todos los ambientes
	 */
	static final String ALFRESCO_PARENT_FOLDER_PATH_TEMPLATES = "/Plantillas"

	/**
	 * PARAMETROS
	 */
	
	/**
	 * Id de los parámetros con datos sensibles
	 */
	static final Integer MAIL_SERVER_USERNAME_ID = 1
	static final Integer MAIL_SERVER_PASSWORD_ID = 2
	static final Integer ALFRESCO_USERNAME_ID = 3
	static final Integer ALFRESCO_PASSWORD_ID = 4

	/**
	 * Identificadores de cierto parámetros almacenados en la base de datos
	 */

	static final Integer PARAM_ID_SENDER_NAME = 5
	/**
	 * Tiempr de recordatorio, actualmente este tiempo se maneja en milisegundos (ms)
	 * 	172800000L // 2 dias
	 * 	120000L // 2 minutos 
	 */
	static final Integer PARAM_ID_REMINDER_TIMER = 6
	static final Integer PARAM_ID_REMINDER_TIMER_PRE = 7
	/**
	 * Url de lista de tareas de Bonita que será agregada a ciertos mensajes enviado por correo electrónico
	 */
	static final Integer PARAM_ID_URL_USERS_APP = 8
	static final Integer PARAM_ID_URL_USERS_APP_PRE = 9

	static final Integer PARAM_ID_URL_WS_FIRMAEC = 10
	static final Integer PARAM_ID_URL_WS_FIRMAEC_PRE = 11
	/**
	 * Headers HTTP con el valor para poder utilizar el servicio web de firmaEC
	 */
	static final Integer PARAM_ID_APYKEY_WS_FIRMAEC = 12
	static final Integer PARAM_ID_URL_ATOMPUB_ALFRESO = 13

}
