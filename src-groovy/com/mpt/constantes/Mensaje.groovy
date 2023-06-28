package com.mpt.constantes

/**
 * Constantes y funciones utilizadas en los mensajes enviados por correo electrónico.
 * 
 * @author alexjcm
 * @version 1.0
 */
class Mensaje {

	static final String LOGO_HEADER = "<img src='https://i.postimg.cc/dVT8Lx6v/logo-unl-cisc.png' \
									height='92' alt='logo-unl-cisc' style='display: block; margin-left: auto; margin-right: auto;'/> <br><br>";
	static final String FOOTER_ADICIONAL = "<h4>NOTA: Imprima este documento, solamente si es necesario. El cuidado del ambiente es T&Uacute; responsabilidad.</h4>";
	static final String SUBJECT_START_NOMENCLATURE = "[MCE #";
	static final String SUBJECT_END_NOMENCLATURE = "] ";
	static final String SUBJECT_SECRETARY_NOTIFICATION = "Solicitud de certificado académico pendiente de revisar"
	static final String SUBJECT_SECRETARY_REMINDER = "Recordatorio de solicitud de certificado académico pendiente de revisar"
	static final String SUBJECT_NOTIFICATION_STUDENT = "Su solicitud de certificado académico ha sido enviada al revisor asignado"
	static final String SUBJECT_NOTIFICATION_STUDENT_REJECTION = "Lo sentimos, su solicitud de certificado académico fue rechazada"
	static final String	SUBJECT_NOTIFICATION_ACCEPTANCE_STUDENT = "Su solicitud de certificado académico fue aceptada"
	static final String SUBJECT_DIRECTOR_REMINDER = "Recordatorio de certificado académico pendiente de firmar"
	static final String	SUBJECT_CERTIFICATE_SENT = "Su certificado solicitado ha sido enviado"
	static final String MESSAGE_HEADER_SECRETARY = "Estimada(o) Secretaria(o)"
	static final String MESSAGE_HEADER_STUDENT = "Estimado(a) estudiante"
	static final String MESSAGE_HEADER_DIRECTOR = "Estimado(a) Director(a)"

	/**
	 * Retorna el código html con la parte incial del cuerpo del mensaje
	 *
	 * @param msgHeader
	 * @return
	 */
	public static String buildMsgHeader(String msgHeader) {
		return "<p style='font-size: 20px;'><strong> ${msgHeader}, </strong></p>"
	}

	/**
	 * Retorna el código html con la parte incial del cuerpo del mensaje concatenado al nombre del estudiante
	 *
	 * @param msgHeader
	 * @param fullName
	 * @return
	 */
	public static String buildMsgHeader(String msgHeader, String fullName) {
		return "<p style='font-size: 20px;'> ${msgHeader} <strong> ${fullName}, </strong></p>"
	}

	/**
	 * Devuelve el código html de botón con el link de acceso a la aplicación de Bonita
	 * 
	 * @param urlApp
	 * @return
	 */
	public static String buildButtonApp(String urlApp) {
		return "<a href=${urlApp} target='_blank' style='color: white; text-decoration: none;'> \
					<center style='background-color: #dd0033; padding: 5px 10px; border-radius:10px;'><h2>Proceso de Solicitud y Emisi&oacute;n de Certificados Acad&eacute;micos</h2></center> \
				</a>"
	}

	/**
	 * Devuelve el código html de botón con el link de acceso a la aplicación de Bonita
	 * 
	 * @param urlApp
	 * @param buttonName
	 * @return
	 */
	public static String buildButtonApp(String urlApp, String buttonName) {
		return "<a href=${urlApp} target='_blank' style='color: white; text-decoration: none;'> \
					<center style='background-color: #dd0033; padding: 5px 10px; border-radius:10px;'><h2>${buttonName}</h2></center> \
				</a>"
	}

	/**
	 * Devuelve el código html de botón con el link de acceso a la aplicación de Bonita
	 *
	 * @param urlApp
	 * @return
	 */
	public static String buildButtonAppConfig(String urlApp) {
		return "<a href=${urlApp} target='_blank' style='color: white; text-decoration: none;'> \
					<center style='background-color: #dd0033; padding: 5px 10px; border-radius:10px;'><h2>Configuraci&oacute;n de par&aacute;metros del MCE</h2></center> \
				</a>"
	}

	/**
	 * Retorna el código html del cuerpo del mensaje
	 * 
	 * @param messageBody
	 * @return
	 */
	public static String buildMessageBody(String messageBody) {
		return "<table align='center' bgcolor='#f4f4f4' cellpadding='30px' width= '70%'> \
			<tr> \
				<td style='background-color: #f4f4f4; font-family: Arial, Helvetica, sans-serif; padding-left: 15%; padding-right: 15%; font-size: 17px;'> ${messageBody} </td> \
			</tr> \
		</table>"
	}

	/**
	 * Retorna el footer principal del mensaje
	 * @param urlUsersApp
	 * @return
	 */
	public static String buildMainFooter(String urlUsersApp) {
		return "<p align='center' style='color: #444444;'><br><br><br>--<br> \
			<b>MODULO AUTOMATIZADO DEL PROCESO DE TITULACION CIS/C</b><br><br> \
			<b>Correo:</b> direccion.cis@unl.<wbr>edu.<wbr>ec<br> <b>Dirección:</b> Ciudad Universitaria \"Ing. Guillermo Falcon&iacute; Espinosa\" <br> \
			<b>Web:</b> <a href=${urlUsersApp} target='_blank' style='color: #444444'>https://computacion.unl.edu.ec/bonita</a><br> \
			<img src='https://i.postimg.cc/vTVQQN1k/logo-unl-negro2.jpg' height='70' alt='logo-unl-negro2'/> <br>--</p>";
	}
}

