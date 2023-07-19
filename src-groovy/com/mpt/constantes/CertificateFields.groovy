package com.mpt.constantes

/**
 * Nombres de los campos de las plantillas de todos los certificados, actualmente son 38 campos en total
 * 
 * @author Allen
 * @version 1.0
 */
class CertificateFields {

	// Campos comunes para la mayoría de certificados:
	static final String STUDENT_NAME = "estudiante"
	static final String STUDENT_NAME_SECOND = "estudiantes"
	static final String STUDENT_ID_CARD = "cedula"
	static final String STUDENT_ID_CARD_SECOND = "cedulas"
	static final String TITULO = "titulo"
	static final String FECHA = "fecha"
	static final String FECHA_FIN = "fechas"
	static final String SECRETARY_NAME = "secretaria"
	static final String GESTOR_NAME = "gestor"
	static final String DOCENTE_NAME = "docente"
	static final String DEAN_NAME = "decano"
	static final String CERTIFICATE_CODE = "certificateCode"
	static final String CAREER_NAME = "carrera"	
	static final String PERTINENCIA = "pertinencia"
	static final String VALIDO = "valido"
	static final String MEMO = "memo"
	static final String MEMO_PASADO = "memos"
	static final String itinerario = "itinerario"
	static final String email = "email"
	

	//
	static final Integer SOLICITUD_PERTINENCIA_ID_1 = 1
	static final String SOLICITUD_PERTINENCIA_NAME_1 = "solocitudPertinencia"
	static final Integer INFORME_PERTINENCIA_ID_2 = 2
	static final String INFORME_PERTINENCIA_NAME_2 = "informePertinencia"
	static final Integer INFORME_DIRECTOR_ID_3 = 3
	static final String INFORME_DIRECTOR_NAME_3 = "informeDirector"
	static final Integer SOLICITUD_PERTINENCIA_ESTUDIANTE_4 = 4
	static final String SOLICITUD_PERTINENCIA_ESTUDIANTE_NAME_4 = "solocitudPertinenciaEstudiante"
	

	/**
	 * Directorios de Alfresco que contienen las plantillas DOCX de los certificados académicos
	 */
	static final String TEMPLATE_SOLICITUD_ESTUDIANTE_PERTINENCIA = "Solicitud_Estudiante_Pertinencia.docx"
	static final String TEMPLATE_SOLICITUD_PERTINENCIA = "Solicitud_Pertinencia.docx"
	static final String TEMPLATE_INFORME_PERTINENCIA = "Informe_Pertinencia.docx"
	static final String TEMPLATE_ASIGNACION_DIRECTOR = "Asignacion_Director.docx"	

	/**
	 * Nombres de los grupos de los funcionarios y estudiantes
	 */
	static final String STUDENT_GROUP_NAME = "estudiante"
	static final String SECRETARY_GROUP_NAME = "secretaria"
	static final String COORDINATION_GROUP_NAME = "coordinacion"
	static final String DECAN_GROUP_NAME = "decanato"

	static final String COMISION_GROUP_NAME = "Comision"
	static final String DOCENTE_GROUP_NAME = "Docentes"
	static final String UNL_GROUP_NAME = "unl"	
	static final String FACULTAD_GROUP_NAME = "facultad"	
	static final String CIS_GROUP_NAME = "cis"
	static final String DIRECCION_GROUP_NAME = "Dirección de Carrera"	
	static final String SSPM_GROUP_NAME = "SSPM"	

	/**
	 * Nombres de los archivo de salida los certificados (tipo docx)
	 */
	static final String FILENAME_PERTINENCIA = "_pertinencia"	
	static final String FILENAME_DIRECTOR = "_director"
	//TODO Puede ser tambien del tribunal, revisar luego
	//static final String FILENAME_TRIBUNAL = "_tribunal"

}
