package com.mpt.constantes

/**
 * Constantes y funciones utilizadas en los mensajes enviados por correo electrónico.
 * 
 * @author alexjcm
 * @version 1.0
 */
class Mensaje {

	static final String LOGO_HEADER = "<img src='https://7e88551e9c.imgdist.com/public/users/Integrators/BeeProAgency/1006022_990869/unnamed.png' \
									height='92' alt='logo-unl-cisc' style='display: block; margin-left: auto; margin-right: auto;'/> <br><br>";
	static final String FOOTER_ADICIONAL = "<h4>NOTA: Imprima este documento, solamente si es necesario. El cuidado del ambiente es Tu responsabilidad.</h4>";
	static final String SUBJECT_START_NOMENCLATURE = "[MPT #";
	static final String SUBJECT_END_NOMENCLATURE = "] ";	
	static final String SUBJECT_NOTIFICATION_DOCENT_PERTINENCIA = "Notificación de asignación para realizar informe de pertinencia"
	static final String SUBJECT_NOTIFICATION_DOCENT_DIRECTOR = "Notificación de asignación como Director de TIC"
	static final String SUBJECT_NOTIFICATION_STUDENT = "La documentación adjuntada es incorrecta"	
	static final String	SUBJECT_NOTIFICATION_STUDENT_DIRECTOR = "Asignación de Director de TIC"
	static final String SUBJECT_NOTIFICATION_STUDENT_REJECTION = "Lo sentimos, su solicitud de obtención de pertinencia fue rechazada"
	static final String SUBJECT_NOTIFICATION_STUDENT_CERTIFICATE = "El certificado de aprobacioón del TIC esta listo."
	static final String	SUBJECT_NOTIFICATION_ACCEPTANCE_STUDENT = "Su solicitud de obtención de pertinencia fue aceptada"
	static final String	SUBJECT_NOTIFICATION_DIRECTOR_PERTINENCIA = "Informe de pertinencia listo para revisión"
	static final String	SUBJECT_NOTIFICATION_DIRECTOR_INCUMPLIMIENTO = "Notificación de incumplimiento de Informe de Pertinencia"
	static final String SUBJECT_DIRECTOR_REMINDER = "Recordatorio de documento pendiente de firmar"
	static final String	SUBJECT_CERTIFICATE_SENT = "Su certificado solicitado ha sido enviado"
	static final String MESSAGE_HEADER_SECRETARY = "Estimada(o) Secretaria(o)"
	static final String MESSAGE_HEADER_STUDENT = "Estimado/s estudiante/s"
	static final String MESSAGE_HEADER_DIRECTOR = "Estimado(a) Director(a)"
	static final String MESSAGE_HEADER_DOCENTE = "Estimado(a) Docente"

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
					<center style='background-color: #dd0033; padding: 5px; border-radius:10px;'><h2>Proceso actual</h2></center> \
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
		/*return "<a href=${urlApp} target='_blank' style='color: white; text-decoration: none;'> \
					<center style='background-color: #dd0033; padding: 5px; border-radius:10px;'><h2>${buttonName}</h2></center> \
				</a>"*/
		String url = "http://localhost:8080/bonita/apps/userAppBonita/task-list/"
		return "<a href=${url} target='_blank' style='color: white; text-decoration: none;'> \
			<center style='background-color: #dd0033; padding: 5px; border-radius:10px;'><h2>${buttonName}</h2></center> \
		</a>"
	}

	
	/**
	 * Retorna el código html del cuerpo del mensaje
	 * 
	 * @param messageBody
	 * @return
	 */
	public static String buildMessageBody(String messageBody) {
		return """<table align='center' bgcolor='#f4f4f4' cellpadding='30px'> 
			<tr> 
				<td style='background-color: #f4f4f4; font-family: Arial, Helvetica, sans-serif; padding-left: 15%; padding-right: 15%; font-size: 17px; text-align: justify;'> ${messageBody} </td> \
			</tr> 
		</table>"""
	}

	/**
	 * Retorna el footer principal del mensaje
	 * @param urlUsersApp
	 * @return
	 */
	public static String buildMainFooter(String urlUsersApp) {
		return "<p align='center' style='color: #444444;'><br><br><br>--<br> \
			<b>MODULO AUTOMATIZADO DEL PROCESO DE TITULACION CIS/C</b><br><br> \
			<b>Correo:</b> direccion.cis@unl.<wbr>edu.<wbr>ec<br> <b>Dirección:</b> Ciudad Universitaria \"Ing. Guillermo Falconi Espinosa\" <br> \
			<b>Web:</b> <a href=${urlUsersApp} target='_blank' style='color: #444444'>#</a><br> \
			<img src='https://i.postimg.cc/vTVQQN1k/logo-unl-negro2.jpg' height='70' alt='logo-unl-negro2'/> <br>--</p>";
	}
}

