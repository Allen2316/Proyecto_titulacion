package com.mpt.constantes

/**
 * Nombres de los campos de las plantillas de todos los certificados
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
	static final String SECRETARIO_ABOGADO = "secretario"
	static final String CERTIFICATE_CODE = "certificateCode"
	static final String CAREER_NAME = "carrera"	
	static final String PERTINENCIA = "pertinencia"
	static final String VALIDO = "valido"
	static final String MEMO = "memo"
	static final String MEMO_PASADO = "memos"
	static final String ITINERARIO = "itinerario"
	static final String EMAIL = "correo"
	static final String EMAIL_SECOND = "correos"
	static final String PORCENTAJE = "porcentaje"
	static final String AUXILIAR = "auxiliar"
	static final String PRESIDENTE = "presidente"
	static final String VOCAL = "vocal"
	static final String VOCALS = "vocales"
	static final String ROL = "rol"
	static final String LUGAR = "lugar"
	static final String DIA = "data"
	static final String MES = "mes"
	static final String ANIO = "anualidad"
	static final String HORA = "hora"
	static final String NOTA = "nota"
	static final String NOTA2 = "nota2"
	static final String NOTA3 = "nota3"
	static final String NOTAS = "notas"
	static final String NOTAR = "notar"
	static final String PROMEDIO = "promedio"
	static final String EQUIVALENCIA = "equivalencia"
	static final String TITULO_PROFESIONAL = "profesional"
	static final String NRO_ACTA = "numero"
	
	

	//
	static final Integer SOLICITUD_PERTINENCIA_ID_1 = 1
	static final String SOLICITUD_PERTINENCIA_NAME_1 = "solocitudPertinencia"
	static final Integer INFORME_PERTINENCIA_ID_2 = 2
	static final String INFORME_PERTINENCIA_NAME_2 = "informePertinencia"
	static final Integer INFORME_DIRECTOR_ID_3 = 3
	static final String INFORME_DIRECTOR_NAME_3 = "informeDirector"
	static final Integer SOLICITUD_PERTINENCIA_ESTUDIANTE_ID_4 = 4
	static final String SOLICITUD_PERTINENCIA_ESTUDIANTE_NAME_4 = "solocitudPertinenciaEstudiante"
	static final Integer SOLICITUD_DIRECTOR_ESTUDIANTE_ID_5 = 5
	static final String SOLICITUD_DIRECTOR_ESTUDIANTE_NAME_5 = "solocitudDirectorEstudiante"
	static final Integer CERTIFICACION_TIC_COMPLETO_ID_6 = 6
	static final String CERTIFICACION_TIC_COMPLETO_NAME = "certificadoTICCompleto"
	static final Integer INFORME_SUSTENTACION_ID_7 = 7
	static final String INFORME_SUSTENTACION_NAME = "informeTribunal"
	static final Integer ACTA_SUSTENTACION_ID_8 = 8
	static final String ACTA_SUSTENTACION_NAME = "actaSustentacion"
	static final Integer INFORME_TIC_ABANDONO_ID_9 = 9
	static final String INFORME_TIC_ABANDONO_NAME = "informeAbandono"
	

	/**
	 * Directorios de Alfresco que contienen las plantillas DOCX de los certificados académicos
	 */
	static final String TEMPLATE_SOLICITUD_ESTUDIANTE_PERTINENCIA = "Solicitud_Estudiante_Pertinencia.docx"
	static final String TEMPLATE_SOLICITUD_PERTINENCIA = "Solicitud_Pertinencia.docx"
	static final String TEMPLATE_INFORME_PERTINENCIA = "Informe_Pertinencia.docx"
	static final String TEMPLATE_ASIGNACION_DIRECTOR = "Asignacion_Director.docx"
	static final String TEMPLATE_SOLICITUD_ESTUDIANTE_DIRECTOR = "Solicitud_Estudiante_Director.docx"
	static final String TEMPLATE_CERTIFICADO_TIC_COMPLETO = "Certificacion_TIC_Completo.docx"
	static final String TEMPLATE_INFORME_ABANDONO = "Certificacion_TIC_Abandonado.docx"
	static final String TEMPLATE_ASIGNACION_TRIBUNAL = "Asignacion_Tribunal.docx"
	static final String TEMPLATE_ACTA_SUSTENTACION = "Acta_Sustentacion.docx"

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
	static final String FILENAME_CERTIFICADO = "_certificado"	
	static final String FILENAME_TRIBUNAL = "_tribunal"
	
	/**
	 * Roles para el tribunal NO asignar a reemplacements dentro de la clase Documento.groovy
	 */
	static final String PRESIDENTE_TRIBUNAL = "Presidente"
	/**
	 * Roles para el tribunal NO asignar a reemplacements dentro de la clase Documento.groovy
	 */
	static final String PRIMER_VOCAL_TRIBUNAL = "Primer Vocal"
	/**
	 * Roles para el tribunal NO asignar a reemplacements dentro de la clase Documento.groovy
	 */
	static final String SEGUNDO_VOCAL_TRIBUNAL = "Segundo Vocal"

}
