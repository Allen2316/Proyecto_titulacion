/**
 * 
 */
package com.mpt.constantes

/**
 * @author Allen
 *
 */
class CertificateFields {

	/**
	 * No se incluye los certificados otros
	 */
	static final Integer TOTAL_INITIAL_CERTIFICATES = 8

	// Campos comunes para la mayoría de certificados:
	static final String STUDENT_NAME = "nombreDelEstudiante"
	static final String STUDENT_ID_CARD = "cedulaDelEstudiante"
	static final String ISSUE_DATE = "fechaDeEmision"
	static final String SECRETARY_NAME = "nombreDeLaSecretaria"
	static final String GESTOR_NAME = "nombreDelGestor"
	static final String DEAN_NAME = "nombreDelDecano"
	static final String CERTIFICATE_CODE = "certificateCode"
	static final String CAREER_NAME = "nombreDeLaCarrera"
	static final String CAREER_NAME_AUX = "nombreDeLaCarreraAux"

	// idCert: 1, Certificado de prácticas pre-profesionales:
	static final Integer CERTIFICATE_ID_1 = 1
	static final String CERTIFICATE_NAME_1= "certPreProfesionales"
	static final String CERTIFICATE_ALFRESCO_FOLDER_1 = "/Certificados_practicas_pre-profesionales"
	static final String C1_MAINTENANCE_COMPANY_NAME = "c1_nombreDeEmpresaMantenimiento"
	static final String C1_MAINTENANCE_START_DATE = "c1_fechaDeInicioMantenimiento"
	static final String C1_MAINTENANCE_END_DATE = "c1_fechaDeFinMantenimiento"
	static final String C1_NETWORKS_COMPANY_NAME = "c1_nombreDeEmpresaRedes"
	static final String C1_NETWORKS_START_DATE = "c1_fechaDeInicioRedes"
	static final String C1_NETWORKS_END_DATE = "c1_fechaDeFinRedes"
	static final String C1_SW_COMPANY_NAME = "c1_nombreDeEmpresaSW"
	static final String C1_SW_START_DATE = "c1_fechaDeInicioSW"
	static final String C1_SW_END_DATE = "c1_fechaDeFinSW"

	// idCert: 2, 3 Certificado de primera o segunda prórroga:
	static final Integer CERTIFICATE_ID_2 = 2
	static final Integer CERTIFICATE_ID_3 = 3
	static final String CERTIFICATE_NAME_2= "certProrroga"
	static final String CERTIFICATE_ALFRESCO_FOLDER_2 = "/Certificados_prorrogas_I_y_II"
	static final String C2_START_STUDIES_DATE = "c2_fechaInicioDeEstudios"
	static final String C2_END_STUDIES_DATE = "c2_fechaFinDeEstudios"
	static final String C2_EXTENSION_NUMBER = "c2_numeroDeProrroga"
	static final String C2_START_DATE_EXTENSION = "c2_fechaInicioProrroga"
	static final String C2_END_DATE_EXTENSION = "c2_fechaFinProrroga"

	// idCert: 4, Certificado de prórroga final para pagos:
	static final Integer CERTIFICATE_ID_4 = 4
	static final String CERTIFICATE_NAME_4= "certProrrogasFinal"
	static final String CERTIFICATE_ALFRESCO_FOLDER_4 = "/Certificados_prorrogas_final"
	static final String C3_START_STUDIES_DATE = "c3_fechaInicioDeEstudios"
	static final String C3_END_STUDIES_DATE = "c3_fechaFinDeEstudios"
	static final String C3_START_DATE_FIRST_EXTENSION = "c3_fechaInicioPrimeraProrroga"
	static final String C3_END_DATE_FIRST_EXTENSION = "c3_fechaFinPrimeraProrroga"
	static final String C3_START_DATE_SECOND_EXTENSION = "c3_fechaInicioSegundaProrroga"
	static final String C3_END_DATE_SECOND_EXTENSION = "c3_fechaFinSegundaProrroga"

	// idCert: 5, 6 Certificado de eventos:
	static final Integer CERTIFICATE_ID_5 = 5
	static final String CERTIFICATE_NAME_5= "certEventoG"
	static final Integer CERTIFICATE_ID_6 = 6
	static final String CERTIFICATE_NAME_6= "certEventoGyD"
	static final String CERTIFICATE_ALFRESCO_FOLDER_6 = "/Certificados_eventos_academicos"
	static final String C4_EVENT_NAME = "c4_nombreDelEvento"
	static final String C4_EVENT_LOCATION = "c4_lugarDelEvento"
	static final String C4_THEME_EVENT = "c4_tematicaDelEvento"
	static final String C4_EVENT_START_DATE = "c4_fechaInicioDelEvento"
	static final String C4_EVENT_END_DATE = "c4_fechaFinDelEvento"
	static final String C4_HOURS_EVENT = "c4_horasDelEvento"
	static final String C4_EVENT_CERT_TYPE = "c4_tipoCertEvento"

	// idCert: 7, Certificado de No Adeudar:
	// Certificado no debe aprobar ocupa solo los datos generales o comunes
	static final Integer CERTIFICATE_ID_7 = 7
	static final String CERTIFICATE_NAME_7= "certNoAdeudar"
	static final String CERTIFICATE_ALFRESCO_FOLDER_7 = "/Certificados_no_adeudar_a_carrera"

	// idCert: 8, Certificado de que no debe aprobar Computación:
	//Certificado no debe aprobar ocupa solo los datos generales o comunes
	static final Integer CERTIFICATE_ID_8 = 8
	static final String CERTIFICATE_NAME_8 = "certNoDebeAprobar"
	static final String CERTIFICATE_ALFRESCO_FOLDER_8 = "/Certificados_no_debe_aprobar_computacion"

	// idCert > 8
	static final String CN_CERTIFICATE_HEADER = "cn_cabeceraDelCertificado"
	static final String CN_CERTIFICATE_BODY = "cn_cuerpoDelCertificado"

	// idCertOtrosGestor
	static final Integer CERTIFICATE_ID_9 = 9
	static final String CERTIFICATE_NAME_9= "certOtrosGestor"
	// idCertOtrosSyG
	static final Integer CERTIFICATE_ID_10 = 10
	static final String CERTIFICATE_NAME_10 = "certOtrosSyG"
	static final String CERTIFICATE_ALFRESCO_FOLDER_10 = "/Certificados_otros"

	/**
	 * Directorios de Alfresco que contienen las plantillas DOCX de los certificados académicos
	 */
	static final String ALFRESCO_CERTIFICATE_TEMPLATES_FOLDER = "/PlantillasDeCertificadosAcademicos/"
	static final String ALFRESCO_CERTIFICATE_TEMPLATES_FOLDER_PRE = "/PlantillasDeCertificadosAcademicosTEST/"
	static final String CERTIFICATE_TEMPLATE_1 = "C01PreProfesionales.docx"
	static final String CERTIFICATE_TEMPLATE_2 = "C02PrimeraOSegundaProrroga.docx"
	static final String CERTIFICATE_TEMPLATE_4 = "C04SegundaProrrogaPagosFinal.docx"
	static final String CERTIFICATE_TEMPLATE_5 = "C05EventoGestor.docx"
	static final String CERTIFICATE_TEMPLATE_6 = "C06EventoGestoryDecano.docx"
	static final String CERTIFICATE_TEMPLATE_7 = "C07NoAdeudar.docx"
	static final String CERTIFICATE_TEMPLATE_8 = "C08NoDebeAprobar.docx"
	static final String CERTIFICATE_TEMPLATE_9 = "C09OtrosGestor.docx"
	static final String CERTIFICATE_TEMPLATE_10 = "C10OtrosSyG.docx"

	/**
	 * Nombres de los grupos de los funcionarios y estudiantes
	 */
	static final String STUDENT_GROUP_NAME = "estudiante"
	static final String SECRETARY_GROUP_NAME = "secretaria"
	static final String COORDINATION_GROUP_NAME = "coordinacion"
	static final String DECAN_GROUP_NAME = "decanato"

	static final String FIRST_EXTENSION = "primera prórroga"
	static final String SECOND_EXTENSION = "segunda prórroga"

	/**
	 * Nombres de los archivo de salida los certificados (tipo docx)
	 */
	static final String FILENAME_CERT_PRACTICES = "-practicas-"
	static final String FILENAME_CERT_FIRST_EXTENSION = "-primera-prorroga-"
	static final String FILENAME_CERT_SECOND_EXTENSION = "-segunda-prorroga-"
	static final String FILENAME_CERT_FINAL_EXTENSION = "-prorroga-final-"
	static final String FILENAME_CERT_EVENT = "-evento-"
	static final String FILENAME_CERT_NON_DEBT = "-no-adeudar-"
	static final String FILENAME_CERT_MUST_NOT_APPROVE = "-no-debe-aprobar-"

}
