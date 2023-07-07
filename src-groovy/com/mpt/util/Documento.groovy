package com.mpt.util

import java.time.LocalDate
import java.util.logging.Logger

import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.bpm.contract.FileInputValue
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.document.DocumentValue

import com.mpt.constantes.CertificateFields;
import com.mpt.constantes.MPTConstants;
import com.mpt.constantes.RequestFields
import com.mpt.payloads.Payload
import com.unl.model.ObjetoCertOtros
import com.unl.model.ObjetoCertificado
import com.unl.model.ObjetoEvento
import com.unl.model.ObjetoPracticas
import com.unl.model.ObjetoProrroga
import com.unl.model.ObjetoProrrogasFinal

/**
 * Métodos para procesar documentos
 * 
 * @author ajcm
 * @version 1.0
 */
class Documento {

	private static final Logger logger = Logger.getLogger(Documento.class.toString())

	/**
	 * Genera un codigo unico para el certificado, combinado en id de la instancia del proceso + el año
	 * actual, para insertar en la plantilla correspondiente y también  anexar al nombre del archivo docx.
	 *
	 * @param processInstanceId
	 * @param addCareerInitials indica si se debe agregar las iniciales de la carrera
	 * @return Codigo generado para el certificado
	 */
	private static String generarCertificateCode(Long processInstanceId, Boolean addCareerInitials) {
		if (!processInstanceId) {
			throw new Exception("Se debe enviar el processInstanceId")
		}

		if (addCareerInitials) {
			return processInstanceId + "-" + LocalDate.now().getYear() + "-" + MPTConstants.CAREER_INITIALS
		}

		return processInstanceId + "-" + LocalDate.now().getYear()
	}

	/**
	 * Genera un codigo unico para la solicitud, combinado en id de la instancia del proceso + el año
	 * actual, para insertar en la plantilla correspondiente y para anexar
	 * al nombre del archivo docx.
	 *
	 * @param processInstanceId
	 * @return Codigo generado para la solicitud
	 */
	private static String generarRequestCode(Long processInstanceId) {
		if (!processInstanceId) {
			throw new Exception("Se debe enviar el processInstanceId")
		}

		return processInstanceId + "-" + LocalDate.now().getYear()
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
	static String generateOutputFilename(Boolean isRequest, Integer idCert, Long idSolicitanteBonitaBPM, Long processInstanceId) {
		if (isRequest == null) {
			throw new Exception("Se debe enviar el isRequest")
		}

		String certificateName = ""
		String documentCode = ""
		String auxIdentifiers = idCert.toString() + idSolicitanteBonitaBPM;
		String timeInMs = System.currentTimeMillis().toString().substring(8) + "-"

		if (isRequest) {
			certificateName = "solicitud-"
			documentCode = generarRequestCode(processInstanceId)
		} else {
			if (idCert == CertificateFields.CERTIFICATE_ID_1) {
				certificateName = CertificateFields.FILENAME_CERT_PRACTICES
			} else if (idCert == CertificateFields.CERTIFICATE_ID_2) {
				certificateName = CertificateFields.FILENAME_CERT_FIRST_EXTENSION
			} else if (idCert == CertificateFields.CERTIFICATE_ID_3) {
				certificateName = CertificateFields.FILENAME_CERT_SECOND_EXTENSION
			} else if (idCert == CertificateFields.CERTIFICATE_ID_4) {
				certificateName = CertificateFields.FILENAME_CERT_FINAL_EXTENSION
			} else if (idCert == CertificateFields.CERTIFICATE_ID_5 || idCert == CertificateFields.CERTIFICATE_ID_6) {
				certificateName = CertificateFields.FILENAME_CERT_EVENT
			} else if (idCert == CertificateFields.CERTIFICATE_ID_7) {
				certificateName = CertificateFields.FILENAME_CERT_NON_DEBT
			} else if (idCert == CertificateFields.CERTIFICATE_ID_8) {
				certificateName = CertificateFields.FILENAME_CERT_MUST_NOT_APPROVE
			}

			certificateName = MPTConstants.BASE_DOCUMENT_NAME + certificateName
			documentCode = generarCertificateCode(processInstanceId, false)
		}

		return certificateName + auxIdentifiers + timeInMs + documentCode + MPTConstants.DOCX_EXTENSION;
	}

	/**
	 * Permite obtener el id del certificado
	 * 
	 * @param certificadoSeleccionado
	 * @return Id del certificado
	 */
	static Integer getCertificateId(ObjetoCertificado certificadoSeleccionado) {
		if (certificadoSeleccionado.idCert > CertificateFields.TOTAL_INITIAL_CERTIFICATES) {
			Boolean firmaSecretariayGestor = certificadoSeleccionado.firmaSecretaria && certificadoSeleccionado.firmaGestor && certificadoSeleccionado.firmaDecano == false
			Boolean firmaSoloGestor = certificadoSeleccionado.firmaGestor && certificadoSeleccionado.firmaSecretaria == false && certificadoSeleccionado.firmaDecano == false

			if (firmaSoloGestor) {
				return CertificateFields.CERTIFICATE_ID_9
			} else if (firmaSecretariayGestor) {
				return CertificateFields.CERTIFICATE_ID_10
			}
		}

		return certificadoSeleccionado.idCert
	}

	/**
	 * Permite obtener la ruta de la plantilla de un documento almacenado en Alfresco (solicitud o certificado)
	 * 
	 * Ejemplo: '/MyCompany/subfolder/document.docx'
	 * @param isRequest
	 * @param esProduccion
	 * @param certificadoSeleccionado
	 * @return
	 */
	static String getDocumentTemplatePathAlfresco(Boolean isRequest, Boolean esProduccion, ObjetoCertificado certificadoSeleccionado) {
		if (isRequest == null) {
			throw new Exception("Se debe enviar isRequest")
		}

		String urlDocumentTemplateAlfresco = ""

		Integer idSelectedCertificate = Documento.getCertificateId(certificadoSeleccionado)

		if (isRequest) {
			if (esProduccion) {
				urlDocumentTemplateAlfresco = MPTConstants.ALFRESCO_PARENT_FOLDER_PATH_TEMPLATES + RequestFields.ALFRESCO_REQUEST_TEMPLATES_FOLDER
			} else {
				urlDocumentTemplateAlfresco = MPTConstants.ALFRESCO_PARENT_FOLDER_PATH_TEMPLATES + RequestFields.ALFRESCO_REQUEST_TEMPLATES_FOLDER_PRE
			}
			
			if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_1) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + RequestFields.REQUEST_TEMPLATE_1
			} else if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_2 || idSelectedCertificate == CertificateFields.CERTIFICATE_ID_3) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + RequestFields.REQUEST_TEMPLATE_2
			} else if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_4) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + RequestFields.REQUEST_TEMPLATE_4
			} else if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_7) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + RequestFields.REQUEST_TEMPLATE_7
			} else if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_8) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + RequestFields.REQUEST_TEMPLATE_8
			} else if (idSelectedCertificate > CertificateFields.TOTAL_INITIAL_CERTIFICATES) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + RequestFields.REQUEST_TEMPLATE_9
			} else {
				logger.severe("---> El certificado seleccionado posee un idCert no válido o inexistente. O el certificado seleccionado no necesita solicitud como los certs. de eventos")
				urlDocumentTemplateAlfresco = ""
			}
		} else {
			if (esProduccion) {
				urlDocumentTemplateAlfresco = MPTConstants.ALFRESCO_PARENT_FOLDER_PATH_TEMPLATES + CertificateFields.ALFRESCO_CERTIFICATE_TEMPLATES_FOLDER
			} else {
				urlDocumentTemplateAlfresco = MPTConstants.ALFRESCO_PARENT_FOLDER_PATH_TEMPLATES + CertificateFields.ALFRESCO_CERTIFICATE_TEMPLATES_FOLDER_PRE
			}
			
			if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_1) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.CERTIFICATE_TEMPLATE_1
			} else if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_2) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.CERTIFICATE_TEMPLATE_2
			} else if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_3) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.CERTIFICATE_TEMPLATE_2
			} else if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_4) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.CERTIFICATE_TEMPLATE_4
			} else if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_5) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.CERTIFICATE_TEMPLATE_5
			} else if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_6) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.CERTIFICATE_TEMPLATE_6
			} else if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_7) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.CERTIFICATE_TEMPLATE_7
			} else if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_8) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.CERTIFICATE_TEMPLATE_8
			} else if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_9) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.CERTIFICATE_TEMPLATE_9
			} else if (idSelectedCertificate == CertificateFields.CERTIFICATE_ID_10) {
				urlDocumentTemplateAlfresco = urlDocumentTemplateAlfresco + CertificateFields.CERTIFICATE_TEMPLATE_10
			} else {
				logger.severe("No existe la plantilla docx con el idCert solicitado")
				urlDocumentTemplateAlfresco = ""
			}
		}

		logger.info("---> urlDocumentTemplateAlfresco: " + urlDocumentTemplateAlfresco)
		return urlDocumentTemplateAlfresco
	}

	/**
	 * Permite obtener los reemplazos para la platilla de la solicitud correspondiente.
	 * 
	 * @param identityAPI
	 * @param idSolicitanteBonitaBPM
	 * @param idCert
	 * @param processInstanceId
	 * @param vpNombreCarrera
	 * @param vpContratoSolProrrogas
	 * @param vpContratoSolOtros
	 * @return Lista de reemplazos para la plantilla de la solicitud
	 */
	static List getRequestTemplateReplacements(IdentityAPI identityAPI, Long idSolicitanteBonitaBPM, Integer idCert, Long processInstanceId, String vpNombreCarrera,
			Map vpContratoSolProrrogas, Map vpContratoSolOtros) {
		List replacementsList = []

		try {
			String fullNameStudent = Estudiante.getFullName(identityAPI, idSolicitanteBonitaBPM)

			// Datos generales de la solicitud
			replacementsList.add([
				RequestFields.REQUEST_CODE,
				generarRequestCode(processInstanceId)
			])
			replacementsList.add([
				CertificateFields.ISSUE_DATE,
				FormatearFecha.obtenerFechaActualFormateada()
			])
			replacementsList.add([
				CertificateFields.GESTOR_NAME,
				Funcionario.getFullName(CertificateFields.COORDINATION_GROUP_NAME, identityAPI)
			])
			replacementsList.add([
				CertificateFields.STUDENT_NAME,
				fullNameStudent
			])
			replacementsList.add([
				CertificateFields.STUDENT_ID_CARD,
				Estudiante.getCedulaStudent(identityAPI, idSolicitanteBonitaBPM)
			])
			replacementsList.add([
				CertificateFields.CAREER_NAME,
				vpNombreCarrera.toUpperCase()
			])
			replacementsList.add([
				CertificateFields.CAREER_NAME_AUX,
				vpNombreCarrera
			])

			// Datos adicionales de acuerdo a la solicitud
			if (idCert == CertificateFields.CERTIFICATE_ID_2 || idCert == CertificateFields.CERTIFICATE_ID_3) {
				replacementsList.add([
					RequestFields.S_EXTENSION_NUMBER,
					vpContratoSolProrrogas.numeroDeProrroga
				])
				replacementsList.add([
					RequestFields.S_TITLE_DEGREE_WORK,
					vpContratoSolProrrogas.tituloTrabajoTitulacion
				])
				replacementsList.add([
					RequestFields.S_DIRECTOR_DEGREE_WORK,
					vpContratoSolProrrogas.directorTrabajoTitulacion
				])
				replacementsList.add([
					RequestFields.S_MONTH_YEAR_START_ACADEMIC_PERIOD,
					FormatearFecha.formatearFecha(vpContratoSolProrrogas.mesYearInicioPeriodoAcademico)
				])
				replacementsList.add([
					RequestFields.S_MONTH_YEAR_END_ACADEMIC_PERIOD,
					FormatearFecha.formatearFecha(vpContratoSolProrrogas.mesYearFinPeriodoAcademico)
				])
			} else if (idCert == CertificateFields.CERTIFICATE_ID_4) {
				replacementsList.add([
					RequestFields.S_TITLE_DEGREE_WORK,
					vpContratoSolProrrogas.tituloTrabajoTitulacion
				])
				replacementsList.add([
					RequestFields.S_DIRECTOR_DEGREE_WORK,
					vpContratoSolProrrogas.directorTrabajoTitulacion
				])
			} else if (idCert > CertificateFields.TOTAL_INITIAL_CERTIFICATES) {
				replacementsList.add([
					RequestFields.S_CERTIFICATE_NAME,
					vpContratoSolOtros.nombreDelCertificado
				])
				replacementsList.add([
					RequestFields.S_REASON_CERTIFICATE_REQUEST,
					vpContratoSolOtros.motivoSolicitudCertificado
				])
			} else {
				logger.info("El certificado seleccionado tiene una solicitud relacionada que no necesita datos adicionales")
			}

			return replacementsList

		} catch (Exception e) {
			throw new Exception("Error: " + e)
		}
	}

	/**
	 * Permite obtener los reemplazos para la platilla del certificado correspondiente.
	 * 
	 * @param identityAPI
	 * @param idSolicitanteBonitaBPM
	 * @param processInstanceId
	 * @param vpNombreCarrera
	 * @param certificadoSeleccionado
	 * @param eventoSeleccionado
	 * @param prorroga
	 * @param prorrogasFinal
	 * @param practicas
	 * @param otros
	 * @return Lista de reemplazos para la plantilla del certificado
	 */
	static List getCertificateTemplateReplacements(IdentityAPI identityAPI, Long idSolicitanteBonitaBPM, Long processInstanceId, String vpNombreCarrera, ObjetoCertificado certificadoSeleccionado,
			ObjetoEvento eventoSeleccionado, ObjetoProrroga prorroga, ObjetoProrrogasFinal prorrogasFinal, ObjetoPracticas practicas, ObjetoCertOtros otros) {
		List replacementsList = []
		try {
			String fullNameStudent = Estudiante.getFullName(identityAPI, idSolicitanteBonitaBPM)

			// Datos generales del certificado
			replacementsList.add([
				CertificateFields.CERTIFICATE_CODE,
				generarCertificateCode(processInstanceId, true)
			])
			replacementsList.add([
				CertificateFields.STUDENT_NAME,
				fullNameStudent
			])
			replacementsList.add([
				CertificateFields.STUDENT_ID_CARD,
				Estudiante.getCedulaStudent(identityAPI, idSolicitanteBonitaBPM)
			])
			replacementsList.add([
				CertificateFields.ISSUE_DATE,
				FormatearFecha.obtenerFechaActualFormateada()
			])
			replacementsList.add([
				CertificateFields.CAREER_NAME,
				vpNombreCarrera.toUpperCase()
			])
			replacementsList.add([
				CertificateFields.CAREER_NAME_AUX,
				vpNombreCarrera
			])

			if (certificadoSeleccionado.firmaSecretaria) {
				replacementsList.add([
					CertificateFields.SECRETARY_NAME,
					Funcionario.getFullName(CertificateFields.SECRETARY_GROUP_NAME, identityAPI)
				])
			}
			if (certificadoSeleccionado.firmaGestor) {
				replacementsList.add([
					CertificateFields.GESTOR_NAME,
					Funcionario.getFullName(CertificateFields.COORDINATION_GROUP_NAME, identityAPI)
				])
			}
			if (certificadoSeleccionado.firmaDecano) {
				replacementsList.add([
					CertificateFields.DEAN_NAME,
					Funcionario.getFullName(CertificateFields.DECAN_GROUP_NAME, identityAPI)
				])
			}

			// Datos adicionales de acuerdo al certificado seleccionado
			if (certificadoSeleccionado.idCert == CertificateFields.CERTIFICATE_ID_1) {
				replacementsList.add([
					CertificateFields.C1_MAINTENANCE_COMPANY_NAME,
					practicas?.nombreDeEmpresaMantenimiento
				])
				replacementsList.add([
					CertificateFields.C1_MAINTENANCE_START_DATE,
					FormatearFecha.formatearFecha(practicas?.fechaDeInicioMantenimiento)
				])
				replacementsList.add([
					CertificateFields.C1_MAINTENANCE_END_DATE,
					FormatearFecha.formatearFecha(practicas?.fechaDeFinMantenimiento)
				])
				replacementsList.add([
					CertificateFields.C1_NETWORKS_COMPANY_NAME,
					practicas?.nombreDeEmpresaRedes
				])
				replacementsList.add([
					CertificateFields.C1_NETWORKS_START_DATE,
					FormatearFecha.formatearFecha(practicas?.fechaDeInicioRedes)
				])
				replacementsList.add([
					CertificateFields.C1_NETWORKS_END_DATE,
					FormatearFecha.formatearFecha(practicas?.fechaDeFinRedes)
				])
				replacementsList.add([
					CertificateFields.C1_SW_COMPANY_NAME,
					practicas?.nombreDeEmpresaSW
				])
				replacementsList.add([
					CertificateFields.C1_SW_START_DATE,
					FormatearFecha.formatearFecha(practicas?.fechaDeInicioSW)
				])
				replacementsList.add([
					CertificateFields.C1_SW_END_DATE,
					FormatearFecha.formatearFecha(practicas?.fechaDeFinSW)
				])
			} else if (certificadoSeleccionado.idCert == CertificateFields.CERTIFICATE_ID_2 || certificadoSeleccionado.idCert == CertificateFields.CERTIFICATE_ID_3) {
				replacementsList.add([
					CertificateFields.C2_START_STUDIES_DATE,
					FormatearFecha.formatearFecha(prorroga?.fechaInicioDeEstudios)
				])
				replacementsList.add([
					CertificateFields.C2_END_STUDIES_DATE,
					FormatearFecha.formatearFecha(prorroga?.fechaFinDeEstudios)
				])
				replacementsList.add([
					CertificateFields.C2_EXTENSION_NUMBER,
					prorroga?.numeroDeProrroga
				])
				replacementsList.add([
					CertificateFields.C2_START_DATE_EXTENSION,
					FormatearFecha.formatearFecha(prorroga?.fechaInicioProrroga)
				])
				replacementsList.add([
					CertificateFields.C2_END_DATE_EXTENSION,
					FormatearFecha.formatearFecha(prorroga?.fechaFinProrroga)
				])
			} else if (certificadoSeleccionado.idCert == CertificateFields.CERTIFICATE_ID_4) {
				replacementsList.add([
					CertificateFields.C3_START_STUDIES_DATE,
					FormatearFecha.formatearFecha(prorrogasFinal?.fechaInicioDeEstudios)
				])
				replacementsList.add([
					CertificateFields.C3_END_STUDIES_DATE,
					FormatearFecha.formatearFecha(prorrogasFinal?.fechaFinDeEstudios)
				])
				replacementsList.add([
					CertificateFields.C3_START_DATE_FIRST_EXTENSION,
					FormatearFecha.formatearFecha(prorrogasFinal?.fechaInicioPrimeraProrroga)
				])
				replacementsList.add([
					CertificateFields.C3_END_DATE_FIRST_EXTENSION,
					FormatearFecha.formatearFecha(prorrogasFinal?.fechaFinPrimeraProrroga)
				])
				replacementsList.add([
					CertificateFields.C3_START_DATE_SECOND_EXTENSION,
					FormatearFecha.formatearFecha(prorrogasFinal?.fechaInicioSegundaProrroga)
				])
				replacementsList.add([
					CertificateFields.C3_END_DATE_SECOND_EXTENSION,
					FormatearFecha.formatearFecha(prorrogasFinal?.fechaFinSegundaProrroga)
				])
			} else if (certificadoSeleccionado.idCert == CertificateFields.CERTIFICATE_ID_5 || certificadoSeleccionado.idCert == CertificateFields.CERTIFICATE_ID_6) {
				replacementsList.add([
					CertificateFields.C4_EVENT_NAME,
					concatenateSpecialSymbol(eventoSeleccionado?.nombre)
				])
				replacementsList.add([
					CertificateFields.C4_EVENT_LOCATION,
					concatenateSpecialSymbol(eventoSeleccionado?.lugar)
				])
				replacementsList.add([
					CertificateFields.C4_THEME_EVENT,
					concatenateSpecialSymbol(eventoSeleccionado?.tematica)
				])
				replacementsList.add([
					CertificateFields.C4_EVENT_START_DATE,
					FormatearFecha.formatearFecha(eventoSeleccionado?.fechaDeInicio)
				])
				replacementsList.add([
					CertificateFields.C4_EVENT_END_DATE,
					FormatearFecha.formatearFecha(eventoSeleccionado?.fechaDeFinalizacion)
				])
				replacementsList.add([
					CertificateFields.C4_HOURS_EVENT,
					eventoSeleccionado?.horas.toString()
				])
				replacementsList.add([
					CertificateFields.C4_EVENT_CERT_TYPE,
					eventoSeleccionado?.tipoCertEvento.nombre
				])
			} else if (certificadoSeleccionado.idCert == CertificateFields.CERTIFICATE_ID_7 || certificadoSeleccionado.idCert == CertificateFields.CERTIFICATE_ID_8) {
				logger.info("El certificado seleccionado no necesita datos adicionales")

			} else if (certificadoSeleccionado.idCert > CertificateFields.TOTAL_INITIAL_CERTIFICATES) {
				replacementsList.add([
					CertificateFields.CN_CERTIFICATE_HEADER,
					otros?.cabecera
				])
				replacementsList.add([
					CertificateFields.CN_CERTIFICATE_BODY,
					otros?.cuerpo
				])
			} else {
				logger.severe("El certificado seleccionado posee un idCert no válido o inexistente.")
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
}
