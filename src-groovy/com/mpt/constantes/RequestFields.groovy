package com.mpt.constantes

/**
 * Nombres de los campos de las plantillas y nombres de las plantillas de todas las solicitudes
 *
 * @author ajcm
 * @version 1.0
 */
class RequestFields {

	/**
	 * No se incluye la solicitud otros
	 */
	static final Integer TOTAL_INITIAL_REQUESTS = 5

	static final String REQUEST_CODE = "requestCode"

	static final String REQUEST_NAME_1 = "solPreProfesionales"
	static final String REQUEST_NAME_2 = "solPrimeraOSegundaProrroga"
	static final String REQUEST_NAME_4 = "solProrrogasFinal"
	static final String REQUEST_NAME_7 = "solNoAdeudar"
	static final String REQUEST_NAME_8 = "solNoAprobarComputacion"
	static final String REQUEST_NAME_9 = "solOtros"

	// Campos para solicitud certificados de primera PRORROGA, segunda prroroga, prorrogas final
	static final String S_EXTENSION_NUMBER = "s_numeroDeProrroga"
	static final String S_TITLE_DEGREE_WORK = "s_tituloTrabajoTitulacion"
	static final String S_DIRECTOR_DEGREE_WORK = "s_directorTrabajoTitulacion"
	static final String S_MONTH_YEAR_START_ACADEMIC_PERIOD = "s_mesYearInicioPeriodoAcademico"
	static final String S_MONTH_YEAR_END_ACADEMIC_PERIOD = "s_mesYearFinPeriodoAcademico"

	// Campos de solicitud para Otros certificados
	static final String S_CERTIFICATE_NAME = "s_nombreDelCertificado"
	static final String S_REASON_CERTIFICATE_REQUEST = "s_motivoSolicitudCertificado"

	/**
	 * Directorios de Alfresco que contienen las plantillas DOCX de las solicitudes
	 */
	static final String ALFRESCO_REQUEST_TEMPLATES_FOLDER = "/PlantillasDeSolicitudes/"
	static final String ALFRESCO_REQUEST_TEMPLATES_FOLDER_PRE = "/PlantillasDeSolicitudesTEST/"
	static final String REQUEST_TEMPLATE_1 = "S01PracticasPreProfesionales.docx"
	static final String REQUEST_TEMPLATE_2 = "S02PrimeraOSegundaProrroga.docx"
	static final String REQUEST_TEMPLATE_4 = "S04ProrrogasFinal.docx"
	static final String REQUEST_TEMPLATE_7 = "S07NoAdeudar.docx"
	static final String REQUEST_TEMPLATE_8 = "S08NoAprobarComputacion.docx"
	static final String REQUEST_TEMPLATE_9 = "S09Otros.docx"

}
