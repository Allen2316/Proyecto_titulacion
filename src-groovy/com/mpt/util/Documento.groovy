package com.mpt.util

import java.time.LocalDate
import java.util.logging.Logger

import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.bpm.contract.FileInputValue
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.document.DocumentValue
import java.util.UUID;
import com.mpt.constantes.CertificateFields;
import com.mpt.util.Funcionario;
import com.mpt.util.Estudiante;
import com.mpt.constantes.MPTConstants;
import com.mpt.payloads.Payload
import com.tic.model.Usuario;
import com.mpt.util.Usuario as UsuarioMPT;
import java.text.DecimalFormat;


/**
 * Métodos para procesar documentos
 * 
 * @author Allen
 * @version 1.0
 */
class Documento {

	private static final Logger logger = Logger.getLogger(Documento.class.toString())

	/**
	 * Genera un codigo unico para el documento, combinado en id de la instancia del proceso + el año
	 * actual, para insertar en la plantilla correspondiente y también  anexar al nombre del archivo docx.
	 *
	 * @param processInstanceId
	 * @param addCareerInitials indica si se debe agregar las iniciales de la carrera
	 * @return Codigo generado para el certificado
	 */
	private static String generarDocumentCode(Long processInstanceId, Boolean addCareerInitials) {
		if (!processInstanceId) {
			throw new Exception("Se debe enviar el processInstanceId")
		}

		if (addCareerInitials) {
			return processInstanceId + "_" + LocalDate.now().getYear() + "_" + MPTConstants.CAREER_INITIALS+ "_M"
		}

		return processInstanceId + "_" + LocalDate.now().getYear() + "_M"
	}


	/**
	 * Permite generar el nombre del archivo (tipo .docx) de salida para una solicitud o un certificado.
	 * Ejemplo de nombre de archivo retornado: solicitud-2585515-19001-2022.docx
	 *
	 * @param isRequest indica si es solicitud o certificado
	 * @param idCert id del certificado
	 * @param idSolicitanteBonitaBPM id del solicitante
	 * @param processInstanceId id de la instancia del proceso
	 * @return filename Nombre del archivo de salida
	 */
	static String generateOutputFilename(Integer idCert, Long idSolicitanteBonitaBPM, Long processInstanceId) {		
		String certificateName = "_"
		String documentCode = ""
		String auxIdentifiers = idCert.toString() + idSolicitanteBonitaBPM;
		String timeInMs = System.currentTimeMillis().toString().substring(8) + "_"
		String tipoName = ""
		
			if (idCert == CertificateFields.SOLICITUD_PERTINENCIA_ID_1 
				|| idCert == CertificateFields.INFORME_PERTINENCIA_ID_2
				|| idCert == CertificateFields.SOLICITUD_PERTINENCIA_ESTUDIANTE_ID_4) {
				//_pertinencia
				tipoName = CertificateFields.FILENAME_PERTINENCIA
			}else if(idCert == CertificateFields.INFORME_DIRECTOR_ID_3){
				//_director
				tipoName = CertificateFields.FILENAME_DIRECTOR
			}
			else if(idCert == CertificateFields.CERTIFICACION_TIC_COMPLETO_ID_6){
				//_certificado
				tipoName = CertificateFields.FILENAME_CERTIFICADO
			}
			else if(idCert == CertificateFields.INFORME_SUSTENTACION_ID_7 
				|| idCert == CertificateFields.ACTA_SUSTENTACION_ID_8){
				//_tribunal
				tipoName = CertificateFields.FILENAME_TRIBUNAL
			}

			certificateName = MPTConstants.BASE_DOCUMENT_NAME + certificateName
			documentCode = generarDocumentCode(processInstanceId, false)		

			//UNL_FEIRNNR_CISC_PO_3537518_13_2023_M_director.docx			
			return certificateName + auxIdentifiers + timeInMs + documentCode + tipoName + MPTConstants.DOCX_EXTENSION;
	}
	
	/**
	 * Obtener el nombre del MEMO par la plantilla .docx, elimina el resto de cadena despues del último "_" presente en la misma
	 * ejm. UNL_FEIRNNR_CISC_PO1511343-3001_2023_M_pertinencia -> UNL_FEIRNNR_CISC_PO1511343-3001_2023_M
	 * @param fileName
	 * @return El nombre del MEMO para plantilla .docx
	 */

	static String generateOutputMemoName(String fileName){
		int indiceUltimoGuionBajo = fileName.lastIndexOf("_");
		if (indiceUltimoGuionBajo >= 0) {
			return fileName.substring(0, indiceUltimoGuionBajo);
		} else {
			return fileName;
		}
	}

	
	/**
	 * Permite obtener la ruta de la plantilla de un documento almacenado en Alfresco
	 * 
	 * Ejemplo: '/MyCompany/plantillas/document.docx'	 
	 * 
	 * @param idTipoInforme
	 * @return
	 */
	static String getDocumentTemplatePathAlfresco(Integer idTipoInforme) {
		String urlDocumentTemplateAlfresco = ""		

		
			urlDocumentTemplateAlfresco = MPTConstants.ALFRESCO_PARENT_FOLDER + MPTConstants.ALFRESCO_PARENT_FOLDER_PATH_TEMPLATES
			
			if (idTipoInforme == CertificateFields.SOLICITUD_PERTINENCIA_ID_1) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.TEMPLATE_SOLICITUD_PERTINENCIA			
			}else if (idTipoInforme == CertificateFields.INFORME_PERTINENCIA_ID_2) {						 
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.TEMPLATE_INFORME_PERTINENCIA
			}else if (idTipoInforme == CertificateFields.INFORME_DIRECTOR_ID_3) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.TEMPLATE_ASIGNACION_DIRECTOR
			}else if (idTipoInforme == CertificateFields.SOLICITUD_PERTINENCIA_ESTUDIANTE_ID_4) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.TEMPLATE_SOLICITUD_ESTUDIANTE_PERTINENCIA
			}else if (idTipoInforme == CertificateFields.SOLICITUD_DIRECTOR_ESTUDIANTE_ID_5) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.TEMPLATE_SOLICITUD_ESTUDIANTE_DIRECTOR
			}else if (idTipoInforme == CertificateFields.CERTIFICACION_TIC_COMPLETO_ID_6) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.TEMPLATE_CERTIFICADO_TIC_COMPLETO			
			}else if (idTipoInforme == CertificateFields.INFORME_SUSTENTACION_ID_7) {
					urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.TEMPLATE_ASIGNACION_TRIBUNAL
			}else if (idTipoInforme == CertificateFields.ACTA_SUSTENTACION_ID_8) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.TEMPLATE_ACTA_SUSTENTACION		
			}else {
				logger.severe("No existe la plantilla docx con el ID solicitado")
				urlDocumentTemplateAlfresco = ""
			}
		

		logger.info("---> urlDocumentTemplateAlfresco: " + urlDocumentTemplateAlfresco)
		return urlDocumentTemplateAlfresco
	}

	/**
	 * Permite obtener los reemplazos para la platilla del DOCUMENTO correspondiente.
	 * 
	 * @param identityAPI
	 * @param idSolicitanteBonitaBPM ID del usuario que inició el proceso
	 * @param idTipoDocumento
	 * @param vpCarrera
	 * @param vpUserNameDocente
	 * @param ticTitulo
	 * @param vpMemoPasado
	 * @param vpMemo
	 * @param pertinencia
	 * @param fecha_fin Segunda fecha (Fecha de culminación, fecha de sustentación, etc)	 
	 * @param itinerario
	 * @param AutorSec Objeto de tipo Usuario que contiene al autor secundario
	 * @param rol El rol dentro del tribunal(presidente, vocales) NO rol de Bonita
	 * @param lugar
	 * @param fecha
	 * @param userNamePresidente Solo para Acta de titulacion
	 * @param userNameVocalP Solo para Acta de titulacion
	 * @param userNameVocalS Solo para Acta de titulacion
	 * @param nota 
	 * @param nota2 
	 * @param nota3
	 * @param notas
	 * @param notar
	 * @param vpTituloProfesional
	 * @param hora    
	 * @return Lista de reemplazos para la plantilla del certificado
	 * 
	 */
	static List getCertificateTemplateReplacements(IdentityAPI identityAPI, Long idSolicitanteBonitaBPM,
													Integer idTipoDocumento, String vpCarrera,
													String vpUserNameDocente, String ticTitulo, 
													String vpMemoPasado, String vpMemo,
													Boolean pertinencia, String fecha_fin,
													String itinerario, Usuario AutorSec,
													String rol, String lugar, String fecha,
													String userNamePresidente, String userNameVocalP,
													String userNameVocalS, String nota,
													String nota2, String nota3,
													Double notas, Double notar,
													String vpTituloProfesional, String hora) {
		List replacementsList = []
		try {
			String fullNameStudent = Estudiante.getFullName(identityAPI, idSolicitanteBonitaBPM)
			String emailStudent = Funcionario.getEmailById(idSolicitanteBonitaBPM, identityAPI, )
			String cedulaStudent = Estudiante.getCedulaStudent(identityAPI, idSolicitanteBonitaBPM)
			String fullNameGestor = Funcionario.getFullName(CertificateFields.COORDINATION_GROUP_NAME, identityAPI)
			String fullNameDocente = Funcionario.getFullNameWithUserName(vpUserNameDocente, identityAPI)
					
			if(AutorSec != null) {
				if(idTipoDocumento == CertificateFields.CERTIFICACION_TIC_COMPLETO_ID_6
					|| idTipoDocumento == CertificateFields.INFORME_TIC_ABANDONO_ID_9) {
					replacementsList.add([
						CertificateFields.AUXILIAR,
						"y el señor/la señorita estudiante "
					])
					
					replacementsList.add([
						CertificateFields.STUDENT_NAME_SECOND,
						Estudiante.getFullName(identityAPI, AutorSec.idSolicitanteBonitaBPM)
					])
				}else {
					replacementsList.add([
						CertificateFields.STUDENT_NAME_SECOND,
						Estudiante.getFullName(identityAPI, AutorSec.idSolicitanteBonitaBPM)
					])
				}
				
			}else {
				replacementsList.add([
					CertificateFields.STUDENT_NAME_SECOND,
					""
				])
			}

			// Datos generales del certificado
			replacementsList.add([
				CertificateFields.MEMO,
				vpMemo
			])					
			
			replacementsList.add([
				CertificateFields.FECHA,
				FormatearFecha.obtenerFechaActualFormateada()
			])
			replacementsList.add([
				CertificateFields.GESTOR_NAME,
				fullNameGestor
			])
			
			replacementsList.add([
				CertificateFields.DOCENTE_NAME,
				fullNameDocente
			])
			
			replacementsList.add([
				CertificateFields.TITULO,
				ticTitulo
			])
						
			
			replacementsList.add([
				CertificateFields.STUDENT_NAME,
				fullNameStudent
			])
			
			replacementsList.add([
				CertificateFields.STUDENT_ID_CARD,
				cedulaStudent
			])
			
			replacementsList.add([
				CertificateFields.CAREER_NAME,
				vpCarrera
			])
						
			
			// Datos adicionales de acuerdo a la solicitud
			if (idTipoDocumento == CertificateFields.INFORME_PERTINENCIA_ID_2) {
				String perti = ""
				String valido = ""
				perti = (pertinencia) ?  "PERTINENTE" : "NO PERTINENTE"
				valido = (pertinencia) ?  "" : "NO"
				
				replacementsList.add([
					CertificateFields.MEMO_PASADO,
					vpMemoPasado
				])
				
				replacementsList.add([
					CertificateFields.PERTINENCIA,
					perti
				])
				
				replacementsList.add([
					CertificateFields.VALIDO,
					valido
				])
			}else if (idTipoDocumento == CertificateFields.INFORME_DIRECTOR_ID_3) {								
				replacementsList.add([
					CertificateFields.FECHA_FIN,
					FormatearFecha.formatearFecha(fecha_fin)
				])
								
			}else if (idTipoDocumento == CertificateFields.SOLICITUD_PERTINENCIA_ESTUDIANTE_ID_4 
					|| idTipoDocumento == CertificateFields.SOLICITUD_DIRECTOR_ESTUDIANTE_ID_5
					|| idTipoDocumento == CertificateFields.CERTIFICACION_TIC_COMPLETO_ID_6
					|| idTipoDocumento == CertificateFields.INFORME_TIC_ABANDONO_ID_9) {								
				replacementsList.add([
					CertificateFields.ITINERARIO,
					itinerario
				])
								
				
				replacementsList.add([
					CertificateFields.EMAIL,
					emailStudent
				])
				
				if(AutorSec != null) {
					replacementsList.add([
						CertificateFields.STUDENT_NAME_SECOND,
						"f: " + Estudiante.getFullName(identityAPI, AutorSec.idSolicitanteBonitaBPM)
					])
					if(idTipoDocumento == CertificateFields.CERTIFICACION_TIC_COMPLETO_ID_6
						|| idTipoDocumento == CertificateFields.INFORME_TIC_ABANDONO_ID_9) {
						replacementsList.add([
							CertificateFields.STUDENT_ID_CARD_SECOND,
							"con C.C. N° " + AutorSec.cedula
						])
					}
					
					replacementsList.add([
						CertificateFields.EMAIL_SECOND,
						"Correo: " + AutorSec.correo						
					])
													
					replacementsList.add([
						CertificateFields.STUDENT_ID_CARD_SECOND,
						"CI: " +AutorSec.cedula
					])
				}else {
					replacementsList.add([
						CertificateFields.STUDENT_NAME_SECOND,
						""
					])
					
					replacementsList.add([
						CertificateFields.EMAIL_SECOND,
						""
					])
													
					replacementsList.add([
						CertificateFields.STUDENT_ID_CARD_SECOND,
						""
					])
				}
														
			}else if (idTipoDocumento == CertificateFields.INFORME_SUSTENTACION_ID_7) {
				replacementsList.add([
					CertificateFields.ROL,
					rol
				])
				replacementsList.add([
					CertificateFields.LUGAR,
					lugar
				])
				
				replacementsList.add([
					CertificateFields.FECHA_FIN,
					FormatearFecha.formatearFecha(fecha_fin) + " a las " + hora
				])
			}else if (idTipoDocumento == CertificateFields.ACTA_SUSTENTACION_ID_8) {
				replacementsList.add([
					CertificateFields.FECHA,
					FormatearFecha.formatearFecha(fecha)
				])
				
				replacementsList.add([
					CertificateFields.FECHA_FIN,
					fecha_fin
				])
				
				replacementsList.add([
					CertificateFields.DIA,
					FormatearFecha.obtenerFechaYHoraSeparada().get("dia")
				])
				replacementsList.add([
					CertificateFields.MES,
					FormatearFecha.obtenerFechaYHoraSeparada().get("mes")
				])
				replacementsList.add([
					CertificateFields.ANIO,
					FormatearFecha.obtenerFechaYHoraSeparada().get("anio")
				])
				replacementsList.add([
					CertificateFields.HORA,
					FormatearFecha.obtenerFechaYHoraSeparada().get("hora")
				])						
				
				String fullNamePresidente = Funcionario.getFullNameWithUserName(userNamePresidente, identityAPI)
				String fullNameVocalP = Funcionario.getFullNameWithUserName(userNameVocalP, identityAPI)
				String fullNameVocalS = Funcionario.getFullNameWithUserName(userNameVocalS, identityAPI)
				
				String fullNameSecreAbo = Funcionario.getFullName("facultad", identityAPI)
				
				replacementsList.add([
					CertificateFields.PRESIDENTE,
					fullNamePresidente
				])
				replacementsList.add([
					CertificateFields.VOCAL,
					fullNameVocalP
				])
				replacementsList.add([
					CertificateFields.VOCALS,
					fullNameVocalS
				])
				replacementsList.add([
					CertificateFields.SECRETARIO_ABOGADO,
					fullNameSecreAbo
				])
				
				replacementsList.add([
					CertificateFields.NOTA,
					nota + "/10 " + "(" + calificacionAstring(Double.parseDouble(nota)) + ")"
				])
				
				replacementsList.add([
					CertificateFields.NOTA2,
					nota2 + "/10 " + "(" + calificacionAstring(Double.parseDouble(nota2)) + ")"
				])

				replacementsList.add([
					CertificateFields.NOTA3,
					nota3 + "/10 " + "(" + calificacionAstring(Double.parseDouble(nota3)) + ")"
				])
				
				// Formatear el resultado con dos decimales sin redondear
				DecimalFormat df = new DecimalFormat("#.##");
				String promedioFormateado = df.format(notas);

				replacementsList.add([
					CertificateFields.NOTAS,
					promedioFormateado + "/10 " + "(" + calificacionAstring(notas) + ")"
				])
								
				replacementsList.add([
					CertificateFields.NOTAR,
					notar + "/10 " + "(" + calificacionAstring(notar) + ")"
				])

				Double promedio2 = (notas + notar)/2
				// Formatear el resultado con dos decimales sin redondear
				DecimalFormat df2 = new DecimalFormat("#.##");
				String promedioFormateado2 = df.format(promedio2);
				
				replacementsList.add([
					CertificateFields.PROMEDIO,
					promedioFormateado2  + "/10 " + "(" + calificacionAstring(promedio2) + ")"
				])
				
				replacementsList.add([
					CertificateFields.EQUIVALENCIA,
					notaAEquivalencia(promedio2)
				])
				
				replacementsList.add([
					CertificateFields.TITULO_PROFESIONAL,
					vpTituloProfesional
				])
				
			}
					

			return replacementsList

		} catch (Exception e) {
			throw new Exception("Error: " + e)
		}
	}
	
	/**
	 * Permite obtener los reemplazos para la platilla del ACTA correspondiente.
	 *
	 * @param identityAPI
	 * @param idSolicitanteBonitaBPM ID del usuario que inició el proceso
	 * @param idTipoDocumento
	 * @param vpCarrera
	 * @param vpUserNameDocente
	 * @param ticTitulo
	 * @param vpMemoPasado
	 * @param vpMemo
	 * @param pertinencia
	 * @param fecha_fin Segunda fecha (Fecha de culminación, fecha de sustentación, etc)
	 * @param itinerario
	 * @param AutorSec Objeto de tipo Usuario que contiene al autor secundario
	 * @param rol El rol dentro del tribunal(presidente, vocales) NO rol de Bonita
	 * @param lugar
	 * @param fecha
	 * @param userNamePresidente Solo para Acta de titulacion
	 * @param userNameVocalP Solo para Acta de titulacion
	 * @param userNameVocalS Solo para Acta de titulacion
	 * @param nota
	 * @param nota2
	 * @param nota3
	 * @param notas
	 * @param notar
	 * @param vpTituloProfesional
	 * @param hora
	 * @return Lista de reemplazos para la plantilla del certificado
	 *
	 */
	static List getCertificateTemplateReplacementsActa(IdentityAPI identityAPI, Long idSolicitanteBonitaBPM,
													Integer idTipoDocumento, String vpCarrera,
													String vpUserNameDocente, String ticTitulo,
													String vpMemoPasado, String vpMemo,
													Boolean pertinencia, String fecha_fin,
													String itinerario, Usuario AutorSec,
													String rol, String lugar, String fecha,
													String userNamePresidente, String userNameVocalP,
													String userNameVocalS, String nota,
													String nota2, String nota3,
													Double notas, Double notar,
													String vpTituloProfesional, String hora,
													Integer nroActa) {
		List replacementsList = []
		try {
			String fullNameStudent = Estudiante.getFullName(identityAPI, idSolicitanteBonitaBPM)
			String emailStudent = Funcionario.getEmailById(idSolicitanteBonitaBPM, identityAPI, )
			String cedulaStudent = Estudiante.getCedulaStudent(identityAPI, idSolicitanteBonitaBPM)
			String fullNameGestor = Funcionario.getFullName(CertificateFields.COORDINATION_GROUP_NAME, identityAPI)
			String fullNameDocente = Funcionario.getFullNameWithUserName(vpUserNameDocente, identityAPI)
					
			if(AutorSec != null) {
				if(idTipoDocumento == CertificateFields.CERTIFICACION_TIC_COMPLETO_ID_6) {
					replacementsList.add([
						CertificateFields.AUXILIAR,
						"y el señor/la señorita estudiante "
					])
					
					replacementsList.add([
						CertificateFields.STUDENT_NAME_SECOND,
						Estudiante.getFullName(identityAPI, AutorSec.idSolicitanteBonitaBPM)
					])
				}else {
					replacementsList.add([
						CertificateFields.STUDENT_NAME_SECOND,
						Estudiante.getFullName(identityAPI, AutorSec.idSolicitanteBonitaBPM)
					])
				}
				
			}else {
				replacementsList.add([
					CertificateFields.STUDENT_NAME_SECOND,
					""
				])
			}

			// Datos generales del certificado
			replacementsList.add([
				CertificateFields.MEMO,
				vpMemo
			])
			
			replacementsList.add([
				CertificateFields.FECHA,
				FormatearFecha.obtenerFechaActualFormateada()
			])
			replacementsList.add([
				CertificateFields.GESTOR_NAME,
				fullNameGestor
			])
			
			replacementsList.add([
				CertificateFields.DOCENTE_NAME,
				fullNameDocente
			])
			
			replacementsList.add([
				CertificateFields.TITULO,
				ticTitulo
			])
						
			
			replacementsList.add([
				CertificateFields.STUDENT_NAME,
				fullNameStudent
			])
			
			replacementsList.add([
				CertificateFields.STUDENT_ID_CARD,
				cedulaStudent
			])
			
			replacementsList.add([
				CertificateFields.CAREER_NAME,
				vpCarrera
			])
						
			
			// Datos adicionales de acuerdo a la solicitud
			if (idTipoDocumento == CertificateFields.INFORME_PERTINENCIA_ID_2) {
				String perti = ""
				String valido = ""
				perti = (pertinencia) ?  "PERTINENTE" : "NO PERTINENTE"
				valido = (pertinencia) ?  "" : "NO"
				
				replacementsList.add([
					CertificateFields.MEMO_PASADO,
					vpMemoPasado
				])
				
				replacementsList.add([
					CertificateFields.PERTINENCIA,
					perti
				])
				
				replacementsList.add([
					CertificateFields.VALIDO,
					valido
				])
			}else if (idTipoDocumento == CertificateFields.INFORME_DIRECTOR_ID_3) {
				replacementsList.add([
					CertificateFields.FECHA_FIN,
					FormatearFecha.formatearFecha(fecha_fin)
				])
								
			}else if (idTipoDocumento == CertificateFields.SOLICITUD_PERTINENCIA_ESTUDIANTE_ID_4
					|| idTipoDocumento == CertificateFields.SOLICITUD_DIRECTOR_ESTUDIANTE_ID_5
					|| idTipoDocumento == CertificateFields.CERTIFICACION_TIC_COMPLETO_ID_6) {
				replacementsList.add([
					CertificateFields.ITINERARIO,
					itinerario
				])
								
				
				replacementsList.add([
					CertificateFields.EMAIL,
					emailStudent
				])
				
				if(AutorSec != null) {
					replacementsList.add([
						CertificateFields.STUDENT_NAME_SECOND,
						"f: " + Estudiante.getFullName(identityAPI, AutorSec.idSolicitanteBonitaBPM)
					])
					if(idTipoDocumento == CertificateFields.CERTIFICACION_TIC_COMPLETO_ID_6) {
						replacementsList.add([
							CertificateFields.STUDENT_ID_CARD_SECOND,
							"con C.C. N° " + AutorSec.cedula
						])
					}
					
					replacementsList.add([
						CertificateFields.EMAIL_SECOND,
						"Correo: " + AutorSec.correo
					])
													
					replacementsList.add([
						CertificateFields.STUDENT_ID_CARD_SECOND,
						"CI: " +AutorSec.cedula
					])
				}else {
					replacementsList.add([
						CertificateFields.STUDENT_NAME_SECOND,
						""
					])
					
					replacementsList.add([
						CertificateFields.EMAIL_SECOND,
						""
					])
													
					replacementsList.add([
						CertificateFields.STUDENT_ID_CARD_SECOND,
						""
					])
				}
														
			}else if (idTipoDocumento == CertificateFields.INFORME_SUSTENTACION_ID_7) {
				replacementsList.add([
					CertificateFields.ROL,
					rol
				])
				replacementsList.add([
					CertificateFields.LUGAR,
					lugar
				])
				
				replacementsList.add([
					CertificateFields.FECHA_FIN,
					FormatearFecha.formatearFecha(fecha_fin) + " a las " + hora
				])
			}else if (idTipoDocumento == CertificateFields.ACTA_SUSTENTACION_ID_8) {
				replacementsList.add([
					CertificateFields.FECHA,
					FormatearFecha.formatearFecha(fecha)
				])
				
				replacementsList.add([
					CertificateFields.FECHA_FIN,
					fecha_fin
				])
				
				replacementsList.add([
					CertificateFields.DIA,
					FormatearFecha.obtenerFechaYHoraSeparada().get("dia")
				])
				replacementsList.add([
					CertificateFields.MES,
					FormatearFecha.obtenerFechaYHoraSeparada().get("mes")
				])
				replacementsList.add([
					CertificateFields.ANIO,
					FormatearFecha.obtenerFechaYHoraSeparada().get("anio")
				])
				replacementsList.add([
					CertificateFields.HORA,
					FormatearFecha.obtenerFechaYHoraSeparada().get("hora")
				])
				
				String fullNamePresidente = Funcionario.getFullNameWithUserName(userNamePresidente, identityAPI)
				String fullNameVocalP = Funcionario.getFullNameWithUserName(userNameVocalP, identityAPI)
				String fullNameVocalS = Funcionario.getFullNameWithUserName(userNameVocalS, identityAPI)
				
				String fullNameSecreAbo = Funcionario.getFullName("facultad", identityAPI)
				
				replacementsList.add([
					CertificateFields.PRESIDENTE,
					fullNamePresidente
				])
				replacementsList.add([
					CertificateFields.VOCAL,
					fullNameVocalP
				])
				replacementsList.add([
					CertificateFields.VOCALS,
					fullNameVocalS
				])
				replacementsList.add([
					CertificateFields.SECRETARIO_ABOGADO,
					fullNameSecreAbo
				])
				
				replacementsList.add([
					CertificateFields.NOTA,
					nota + "/10 " + "(" + calificacionAstring(Double.parseDouble(nota)) + ")"
				])
				
				replacementsList.add([
					CertificateFields.NOTA2,
					nota2 + "/10 " + "(" + calificacionAstring(Double.parseDouble(nota2)) + ")"
				])

				replacementsList.add([
					CertificateFields.NOTA3,
					nota3 + "/10 " + "(" + calificacionAstring(Double.parseDouble(nota3)) + ")"
				])
				
				// Formatear el resultado con dos decimales sin redondear
				DecimalFormat df = new DecimalFormat("#.##");
				String promedioFormateado = df.format(notas);

				replacementsList.add([
					CertificateFields.NOTAS,
					promedioFormateado + "/10 " + "(" + calificacionAstring(notas) + ")"
				])
								
				replacementsList.add([
					CertificateFields.NOTAR,
					notar + "/10 " + "(" + calificacionAstring(notar) + ")"
				])

				Double promedio2 = (notas + notar)/2
				// Formatear el resultado con dos decimales sin redondear
				DecimalFormat df2 = new DecimalFormat("#.##");
				String promedioFormateado2 = df.format(promedio2);
				
				replacementsList.add([
					CertificateFields.PROMEDIO,
					promedioFormateado2  + "/10 " + "(" + calificacionAstring(promedio2) + ")"
				])
				
				replacementsList.add([
					CertificateFields.EQUIVALENCIA,
					notaAEquivalencia(promedio2)
				])
				
				replacementsList.add([
					CertificateFields.TITULO_PROFESIONAL,
					vpTituloProfesional
				])
				
				replacementsList.add([
					CertificateFields.NRO_ACTA,
					nroActa
				])
				
			}
					

			return replacementsList

		} catch (Exception e) {
			throw new Exception("Error: " + e)
		}
	}

	/**
	 * Permite actualizar el pdf firmado
	 * 
	 * @param groupName
	 * @param contratoReobtenerToken
	 * @param contratoFirmaElectronicamente
	 * @param contratoUrlPdfFirmado
	 * @param pdfGenerado
	 * @param pdfFirmado
	 * @param contratoPdfSolFirmadaEx
	 * @return
	 */
	static DocumentValue updatePDFSigned(String groupName, Boolean contratoReobtenerToken, Boolean contratoFirmaElectronicamente, String contratoUrlPdfFirmado,
			Document pdfGenerado, Document pdfFirmado, FileInputValue contratoPdfSolFirmadaEx) {
		if (groupName.isEmpty()) {
			throw new Exception("Se debe enviar el groupName")
		}

		try {
			String futurePDFName = ""

			if (groupName.equals(CertificateFields.STUDENT_GROUP_NAME)) {
				if (contratoReobtenerToken == Boolean.FALSE && contratoFirmaElectronicamente == Boolean.TRUE) {
					// Convertimos a bytes el pdf de la url
					byte[] contentMyPDF = contratoUrlPdfFirmado.toURL().getBytes()
					futurePDFName = Payload.getFutureFilenamePDF(pdfGenerado.getContentFileName())
					logger.info("----> El estudiante firma la solicitud electrónicamente")
					
					/*DocumentValue doc = new DocumentValue(contentMyPDF, MPTConstants.MYME_TYPE_PDF, futurePDFName)
					doc.setDocumentId(generateDocumentId())
					return doc*/
					return new DocumentValue(contentMyPDF, MPTConstants.MYME_TYPE_PDF, futurePDFName)

				} else if (contratoReobtenerToken == Boolean.FALSE && contratoFirmaElectronicamente == Boolean.FALSE) {
					logger.info("----> El estudiante firma la solicitud externamente")

					// From a contractInput
					return new DocumentValue(contratoPdfSolFirmadaEx.content, contratoPdfSolFirmadaEx.contentType, contratoPdfSolFirmadaEx.fileName)

				} else if (contratoReobtenerToken == Boolean.TRUE && contratoFirmaElectronicamente == Boolean.TRUE) {
					// No importa retornar un valor nulo para actualizar pdfFirmado por que el estudiante siempre usa el pdfGenerado
					return null
				}
			}


			if (groupName.equals(CertificateFields.SECRETARY_GROUP_NAME)) {
				if (contratoReobtenerToken == false) {
					// Convertimos a bytes el pdf de la url
					byte[] contentMyPDF = contratoUrlPdfFirmado.toURL().getBytes()
					// Si la secretaria va a firmar siempre se utiliza el pdfGenerado
					futurePDFName = Payload.getFutureFilenamePDF(pdfGenerado.getContentFileName())

					return new DocumentValue(contentMyPDF, MPTConstants.MYME_TYPE_PDF, futurePDFName)

				} else if (contratoReobtenerToken == true) {
					// No importa retornar un valor nulo para actualizar pdfFirmado por que la Secretaria siempre usa el pdfGenerado
					return null
				}
			}


			if (groupName.equals(CertificateFields.COORDINATION_GROUP_NAME)) {
				if (contratoReobtenerToken == false) {
					// Convertimos a bytes el pdf de la url
					byte[] contentMyPDF = contratoUrlPdfFirmado.toURL().getBytes()
					// Obtenemos el futuro nombre del pdf generado o firmado segun sea el caso
					futurePDFName = pdfFirmado == null ? Payload.getFutureFilenamePDF(pdfGenerado.getContentFileName()) : Payload.getFutureFilenamePDF(pdfFirmado.getContentFileName())

					return new DocumentValue(contentMyPDF, MPTConstants.MYME_TYPE_PDF, futurePDFName)

				} else if (contratoReobtenerToken == true) {
					if (pdfFirmado == null) {
						logger.warning("pdfFirmado SI es nulo. No importa retornar un valor nulo para actualizar pdfFirmado por que en este caso el Gestor usa el pdfGenerado")
						return null
					}
					logger.warning("pdfFirmado NO es nulo. Se debe retornar el mismo pdfFirmado intacto para ser utilizado por esta misma tarea luego de reobtener el token")

					return new DocumentValue(pdfFirmado.getId())
				}
			}


			if (groupName.equals(CertificateFields.DECAN_GROUP_NAME)) {
				if (contratoReobtenerToken == false) {
					// Convertimos a bytes el pdf de la url
					byte[] contentMyPDF = contratoUrlPdfFirmado.toURL().getBytes()
					// El Decano siempre recibe el pdf Firmado
					futurePDFName = Payload.getFutureFilenamePDF(pdfFirmado.getContentFileName())

					return new DocumentValue(contentMyPDF, MPTConstants.MYME_TYPE_PDF, futurePDFName)

				} else if (contratoReobtenerToken == true) {
					// Siempre se debe retornar el mismo pdfFirmado intacto para ser utilizado por esta misma tarea luego de reobtener el token
					return new DocumentValue(pdfFirmado.getId())
				}
			}

			logger.warning("El nombre de grupo enviado no existe")
			return null

		} catch (Exception iae) {
			logger.severe("Ha ocurrido un error: "  + iae)
			throw new IllegalArgumentException("Error: " + iae)
		}
	}
	
	
	
	/**
	 * Genera un ID para el documento firmado
	 * @return el UUID en Long
	 */
	static Long generateDocumentId() {
		UUID uuid = UUID.randomUUID()		
		return uuid.getMostSignificantBits() < 0 ? uuid.getMostSignificantBits() * -1 :uuid.getMostSignificantBits()
	}
	
	/**
	 * Concatena al simbolo especial & los caracteres amp; para luego ser insertado en la plantilla docx.
	 * Es utilizado para los certificados de eventos
	 * 
	 * @param text
	 * @return
	 */
	private static String concatenateSpecialSymbol(String text) {
		if (text.contains("&")) {
			logger.warning("El texto contiene el caracter especial &")
			
			return text.replaceAll("&", "&amp;")
		}

		return text;
	}
	
	/**
	 * Obtiene la equivalencia de la nota de acuerdo al promedio ingresado.
	 * @param promedio El promedio de las notas.
	 * @return La equivalencia de la nota (Reprobado, Buena, Muy Buena, Sobresaliente) según el promedio.
	 */
	private static String notaAEquivalencia(Double promedio) {
		if (promedio < 7.0) {
			return "Reprobado";
		} else if (promedio >= 7.0 && promedio <= 7.99) {
			return "Buena";
		} else if (promedio >= 8.0 && promedio <= 8.99) {
			return "Muy Buena";
		} else if (promedio >= 9.0 && promedio <= 10.0) {
			return "Sobresaliente";
		} else {
			return "";
		}
	}
		
	
	
	/**
	 * Convierte una calificación de formato "X.Y" a letras en español.
	 *
	 * @param calificacion La calificación en formato decimal (por ejemplo, 8.93).
	 * @return Una cadena que representa la calificación en letras (por ejemplo, "OCHO PUNTO NOVENTA Y TRES SOBRE DIEZ").
	 */
	private static String calificacionAstring(Double calificacion) {
	    String[] UNIDADES = ["", "UNO", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE"];
	    String[] DECENAS = ["", "DIEZ", "VEINTE", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"];
	    String[] ESPECIALES = ["", "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISEIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE"];
	
	    if (calificacion < 0 || calificacion > 10) {
	        return "Calificación fuera de rango";
	    }
	
	    int parteEntera = (int) calificacion;
	    double parteDecimal = calificacion - parteEntera;
	
	    String letrasParteEntera = UNIDADES[parteEntera];
	    String letrasParteDecimal = "";
	
	    if (parteDecimal > 0) {
	        int parteDecimalRedondeada = (int) Math.round(parteDecimal * 100);
	
	        if (parteDecimalRedondeada < 10) {
	            letrasParteDecimal = UNIDADES[parteDecimalRedondeada];
	        } else if (parteDecimalRedondeada < 20) {
	            letrasParteDecimal = ESPECIALES[parteDecimalRedondeada - 10];
	        } else {
	            int unidades = parteDecimalRedondeada % 10;
	            int decenas = parteDecimalRedondeada / 10;
	            letrasParteDecimal = DECENAS[decenas];
	            if (unidades > 0) {
	                letrasParteDecimal += " Y " + UNIDADES[unidades];
	            }
	        }
	    }

	    // Agregar la parte decimal solo si es mayor que cero
	    String parteDecimalStr = parteDecimal > 0 ? " PUNTO " + letrasParteDecimal : " PUNTO CERO ";
	    return letrasParteEntera + parteDecimalStr + " SOBRE DIEZ";
	}
	
	
}
