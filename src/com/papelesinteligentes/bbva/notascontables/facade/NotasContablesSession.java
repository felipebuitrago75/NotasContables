package com.papelesinteligentes.bbva.notascontables.facade;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.papelesinteligentes.bbva.notascontables.carga.dto.ISalida;
import com.papelesinteligentes.bbva.notascontables.carga.dto.PartidaPendiente;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RechazoSalida;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Sucursal;
import com.papelesinteligentes.bbva.notascontables.dto.ActividadRealizada;
import com.papelesinteligentes.bbva.notascontables.dto.Anexo;
import com.papelesinteligentes.bbva.notascontables.dto.Auditoria;
import com.papelesinteligentes.bbva.notascontables.dto.AuditoriaDetalle;
import com.papelesinteligentes.bbva.notascontables.dto.CausalDevolucion;
import com.papelesinteligentes.bbva.notascontables.dto.CentroEspecial;
import com.papelesinteligentes.bbva.notascontables.dto.CommonVO;
import com.papelesinteligentes.bbva.notascontables.dto.Concepto;
import com.papelesinteligentes.bbva.notascontables.dto.CuentaCOD;
import com.papelesinteligentes.bbva.notascontables.dto.EnteAutorizador;
import com.papelesinteligentes.bbva.notascontables.dto.FechaHabilitada;
import com.papelesinteligentes.bbva.notascontables.dto.Impuesto;
import com.papelesinteligentes.bbva.notascontables.dto.Instancia;
import com.papelesinteligentes.bbva.notascontables.dto.Menu;
import com.papelesinteligentes.bbva.notascontables.dto.MontoAutorizado;
import com.papelesinteligentes.bbva.notascontables.dto.MontoAutorizadoGeneral;
import com.papelesinteligentes.bbva.notascontables.dto.MontoMaximo;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContable;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableCrucePartidaPendiente;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableRegistroLibre;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTema;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTemaImpuesto;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTotal;
import com.papelesinteligentes.bbva.notascontables.dto.Observacion;
import com.papelesinteligentes.bbva.notascontables.dto.Padrino;
import com.papelesinteligentes.bbva.notascontables.dto.Parametro;
import com.papelesinteligentes.bbva.notascontables.dto.RiesgoOperacional;
import com.papelesinteligentes.bbva.notascontables.dto.Rol;
import com.papelesinteligentes.bbva.notascontables.dto.SubMenu;
import com.papelesinteligentes.bbva.notascontables.dto.SubMenuRol;
import com.papelesinteligentes.bbva.notascontables.dto.Tema;
import com.papelesinteligentes.bbva.notascontables.dto.TemaAutorizacion;
import com.papelesinteligentes.bbva.notascontables.dto.TemaImpuesto;
import com.papelesinteligentes.bbva.notascontables.dto.TemaProducto;
import com.papelesinteligentes.bbva.notascontables.dto.TipoEvento;
import com.papelesinteligentes.bbva.notascontables.dto.UnidadAnalisis;
import com.papelesinteligentes.bbva.notascontables.dto.UsuarioInstancias;
import com.papelesinteligentes.bbva.notascontables.dto.UsuarioModulo;
import com.papelesinteligentes.bbva.notascontables.jsf.beans.UsuarioLogueado;

/**
 * Remote interface for Enterprise Bean: NotasContablesSession
 */
public interface NotasContablesSession {

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Auditoria> getRegistrosAuditoria() throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addRol(Rol row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateRol(Rol row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteRol(Rol row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoRol(Rol row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Rol getRol(Rol row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Rol> getRolesPorEstado(Rol row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Rol> getRoles() throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addUsuarioModulo(UsuarioModulo row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateUsuarioModulo(UsuarioModulo row, int codigoUsuario, boolean reasignarPendientes) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteUsuarioModulo(UsuarioModulo row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoUsuarioModulo(UsuarioModulo row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public UsuarioModulo getUsuarioModulo(UsuarioModulo row) throws Exception;

	public UsuarioModulo getUsuarioByCodEmpAndRol(String codigoEmp, int codigoRol) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<UsuarioModulo> getUsuariosModuloPorEstado(UsuarioModulo row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<UsuarioModulo> getUsuariosModulo() throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addTemaAutorizacion(TemaAutorizacion row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateTemaAutorizacion(TemaAutorizacion row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteTemaAutorizacion(TemaAutorizacion row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoTemaAutorizacion(TemaAutorizacion row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public TemaAutorizacion getTemaAutorizacion(TemaAutorizacion row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<TemaAutorizacion> getTemasAutorizacionPorEstado(TemaAutorizacion row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<TemaAutorizacion> getTemasAutorizacion() throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addCausalDevolucion(CausalDevolucion row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateCausalDevolucion(CausalDevolucion row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteCausalDevolucion(CausalDevolucion row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoCausalDevolucion(CausalDevolucion row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public CausalDevolucion getCausalDevolucion(CausalDevolucion row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<CausalDevolucion> getCausalesDevolucionPorEstado(CausalDevolucion row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<CausalDevolucion> getCausalesDevolucion() throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addTipoEvento(TipoEvento row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateTipoEvento(TipoEvento row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteTipoEvento(TipoEvento row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoTipoEvento(TipoEvento row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public TipoEvento getTipoEvento(TipoEvento row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<TipoEvento> getTiposEventoPorEstado(TipoEvento row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<TipoEvento> getTiposEvento() throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addEnteAutorizador(EnteAutorizador row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateEnteAutorizador(EnteAutorizador row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteEnteAutorizador(EnteAutorizador row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoEnteAutorizador(EnteAutorizador row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public EnteAutorizador getEnteAutorizador(EnteAutorizador row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<EnteAutorizador> getEntesAutorizadoresPorEstado(EnteAutorizador row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<EnteAutorizador> getEntesAutorizadores() throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addMontoAutorizado(MontoAutorizado row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateMontoAutorizado(MontoAutorizado row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteMontoAutorizado(MontoAutorizado row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoMontoAutorizado(MontoAutorizado row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public MontoAutorizado getMontoAutorizado(MontoAutorizado row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<MontoAutorizado> getMontosAutorizadosPorEstado(MontoAutorizado row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<MontoAutorizado> getMontosAutorizados() throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addFechaHabilitada(FechaHabilitada row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateFechaHabilitada(FechaHabilitada row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteFechaHabilitada(FechaHabilitada row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public FechaHabilitada getFechaHabilitada(FechaHabilitada row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<FechaHabilitada> getFechasHabilitadas() throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addImpuesto(Impuesto row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateImpuesto(Impuesto row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteImpuesto(Impuesto row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoImpuesto(Impuesto row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Impuesto getImpuesto(Impuesto row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Impuesto> getImpuestosPorEstado(Impuesto row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Impuesto> getImpuestos() throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addPadrino(Padrino row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updatePadrino(Padrino row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deletePadrino(Padrino row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoPadrino(Padrino row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Padrino getPadrino(Padrino row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Padrino> getPadrinosPorEstado(Padrino row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Padrino> getPadrinos() throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addUnidadAnalisis(UnidadAnalisis row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateUnidadAnalisis(UnidadAnalisis row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteUnidadAnalisis(UnidadAnalisis row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoUnidadAnalisis(UnidadAnalisis row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public UnidadAnalisis getUnidadAnalisis(UnidadAnalisis row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<UnidadAnalisis> getUnidadesAnalisisPorEstado(UnidadAnalisis row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<UnidadAnalisis> getUnidadesAnalisis() throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<CausalDevolucion> searchCausalDevolucion(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<EnteAutorizador> searchEnteAutorizador(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<FechaHabilitada> searchFechaHabilitada(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Impuesto> searchImpuesto(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<MontoAutorizado> searchMontoAutorizado(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Padrino> searchPadrino(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Rol> searchRol(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<TemaAutorizacion> searchTemaAutorizacion(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<TipoEvento> searchTipoEvento(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<UnidadAnalisis> searchUnidadAnalisis(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<UsuarioModulo> searchUsuarioModulo(String palabraClave) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addConcepto(Concepto row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateConcepto(Concepto row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteConcepto(Concepto row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoConcepto(Concepto row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Concepto getConcepto(Concepto row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Concepto> getConceptosPorEstado(Concepto row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Concepto> getConceptos() throws Exception;

	/**
	 * @param codigoTema
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<TemaProducto> getProductosPorTema(int codigoTema) throws Exception;

	/**
	 * @param row
	 * @param impuestos
	 * @param productos
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addTema(Tema row, Collection<TemaImpuesto> impuestos, Collection<TemaProducto> productos, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param impuestos
	 * @param productos
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateTema(Tema row, Collection<TemaImpuesto> impuestos, Collection<TemaProducto> productos, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteTema(Tema row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoTema(Tema row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Tema getTema(Tema row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Tema> getTemasPorEstado(Tema row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Tema> getTemasPorConcepto(Tema row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Tema> getTemasPorConceptoYEstado(Tema row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Tema> getTemas() throws Exception;

	/**
	 * @param codigoTema
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<TemaImpuesto> getImpuestosPorTema(int codigoTema) throws Exception;

	/**
	 * @param impuestosTema
	 * @param codigoImpuesto
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public boolean isImpuestoIncluido(Collection<TemaImpuesto> impuestosTema, int codigoImpuesto) throws java.rmi.RemoteException;

	/**
	 * @param productosTema
	 * @param codigoProducto
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public boolean isProductoIncluido(Collection<TemaProducto> productosTema, String codigoProducto) throws java.rmi.RemoteException;

	/**
	 * @param codigoArea
	 * @param codigoRol
	 * @param estado
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<UsuarioModulo> getUsuariosModuloPorAreaYRolYEstado(String codigoArea, int codigoRol, String estado) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public UsuarioModulo getUsuarioModuloPorCodigoEmpleado(UsuarioModulo row) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addCentroEspecial(CentroEspecial row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateCentroEspecial(CentroEspecial row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteCentroEspecial(CentroEspecial row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoCentroEspecial(CentroEspecial row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public CentroEspecial getCentroEspecial(CentroEspecial row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<CentroEspecial> getCentrosEspecialesPorEstado(CentroEspecial row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<CentroEspecial> getCentrosEspeciales() throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<CentroEspecial> searchCentroEspecial(String palabraClave) throws Exception;

	/**
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws CreateException
	 * @throws Exception
	 */
	public void loadFechasHabilitadas() throws Exception;

	/**
	 * @param codigoTemaNotaContable
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<NotaContableTemaImpuesto> getImpuestosPorTemaNotaContable(int codigoTemaNotaContable) throws Exception;

	/**
	 * @param codigoNotaContable
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<NotaContableTema> getTemasPorNotaContable(int codigoNotaContable) throws Exception;

	/**
	 * @param codigoNotaContable
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<NotaContableTotal> getTotalesPorNotaContable(int codigoNotaContable) throws Exception;

	/**
	 * @param codigoConcepto
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Tema> searchTema(String palabraClave) throws Exception;

	/**
	 * @param sucursalesCentro
	 * @param codigoSucursal
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public boolean isSucursalIncluida(Collection<Sucursal> sucursalesCentro, String codigoSucursal) throws java.rmi.RemoteException;

	/**
	 * @param dias
	 * @param opcion
	 * @param sucursales
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateTodasFechasHabilitadas(int dias, String opcion, String[] sucursales, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Concepto> getConceptosPorEstadoParaConcepto(Concepto row) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateInstancia(Connection con, Instancia row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteInstancia(Connection con, Instancia row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Instancia getInstancia(Instancia row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Instancia> getInstanciasPorEstado(Instancia row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Instancia> getInstanciasPorUsuario(Instancia row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Instancia> getInstanciasPorEstadoYUsuario(Instancia row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Instancia> getInstancias() throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addActividadRealizada(Connection con, ActividadRealizada row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public ActividadRealizada getActividadRealizada(ActividadRealizada row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Observacion getObservacion(Observacion row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Observacion> getObservacionesPorTema(Observacion row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<ActividadRealizada> getActividadesRealizadasPorInstancia(ActividadRealizada row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public CentroEspecial getCentroEspecialPorSucursal(CentroEspecial row) throws Exception;

	/**
	 * @param palabraClave
	 * @param tipoArea
	 * @param codigoArea
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Concepto> searchConceptoParaNotaContable(String palabraClave, String tipoArea, String codigoArea) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addAnexo(Connection con, Anexo row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Anexo> getAnexosPorTema(Anexo row) throws Exception;

	// gp12833 - aseguramiento anexos
	public Collection<Anexo> getAnexosPorTemaORadicado(Anexo row, String numeroRadicacion) throws Exception;
	
	public Collection<Anexo> findByNumeroRadicacion(String numeroRadicacion) throws Exception;
	// fin gp12833 - aseguramiento anexos
	/**
	 * @param row
	 * @param codigoUsuario
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public int addInstancia(Connection con, Instancia row, int codigoUsuario) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Concepto> searchConceptoByNombre(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Auditoria> searchRegistrosAuditoria(String palabraClave) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addCuentaCOD(CuentaCOD row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateCuentaCOD(CuentaCOD row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteCuentaCOD(CuentaCOD row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public CuentaCOD getCuentaCOD(CuentaCOD row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<CuentaCOD> getCuentasCOD() throws Exception;

	/**
	 * @param codigoAuditoria
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public AuditoriaDetalle getDetalleAuditoriaPorCodigoAuditoria(int codigoAuditoria) throws Exception;

	/**
	 * @param codigoAuditoria
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Auditoria getRegistroAuditoria(int codigoAuditoria) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<CuentaCOD> searchCuentaCOD(String palabraClave) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public EnteAutorizador getEntesAutorizadoresPorSucursalYEstado(EnteAutorizador row) throws Exception;

	/**
	 * @param codigoRol
	 * @param estado
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<UsuarioModulo> getUsuariosModuloPorRolYEstado(int codigoRol, String estado) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Padrino> getPadrinosPorUnidadAnalisisYEstado(Padrino row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Instancia> getInstanciasPorSucursalOrigen(Instancia row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Instancia> getInstanciasPorEstadoYSucursalOrigen(Instancia row) throws Exception;

	/**
	 * @param codigoNotaContable
	 * @param codigoTema
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public NotaContableTema getTemaNotaContablePorNotaContableYTema(int codigoNotaContable, int codigoTema) throws Exception;

	public NotaContableTema getTemaNotaContablePorCodigo(NotaContableTema row) throws Exception;

	public Collection<NotaContableTema> getTemaNotaContablePorConceptoEstadoYTema(int codigoNotaContable, String estado, int codigoTema) throws Exception;

	/**
	 * @param codigoNotaContable
	 * @param codigoTemaNotaContable
	 * @param codigoImpuesto
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public NotaContableTemaImpuesto getImpuestoPorNotaContableYTemaNotaContableYImpuesto(int codigoNotaContable, int codigoTemaNotaContable, int codigoImpuesto) throws Exception;

	/**
	 * @param row
	 * @param montoNotaContable
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public MontoAutorizado getMontoAutorizadoPorTemaAutorizacionYTipoEventoNotaContableYEstado(MontoAutorizado row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Instancia getInstanciaPorNotaContable(Instancia row) throws Exception;

	/**
	 * @param instancia
	 * @param codigoUsuario
	 * @param aprobada
	 * @param codigoCausalDevolucion
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public String verificarUsuarioSiguienteActividad(Instancia instancia, int codigoUsuario, boolean aprobada, int codigoCausalDevolucion) throws Exception;

	/**
	 * @param codigoTemaNotaContable
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<RiesgoOperacional> getRiesgoPorTemaNotaContable(int codigoTemaNotaContable) throws Exception;

	/**
	 * @param codigoNotaContable
	 * @param codigoTemaNotaContable
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public RiesgoOperacional getRiesgoPorNotaContableYTemaNotaContable(int codigoNotaContable, int codigoTemaNotaContable) throws Exception;

	/**
	 * @param instancia
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void anularNotaContable(Instancia instancia, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Instancia> getInstanciasPorSucursalOrigenYFechas(Instancia row, Date fechaDesde, Date fechaHasta) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<UsuarioModulo> getUsuariosModuloPorCodigoEmpleado(UsuarioModulo row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public CuentaCOD getCuentaCODPorCuentaContable(CuentaCOD row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Tema> getTemasPorPartidaOContraPartida(Tema row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Padrino getPadrinoPorUsuario(Padrino row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public EnteAutorizador getEnteAutorizadorPorUsuario(EnteAutorizador row) throws Exception;

	public void siguienteActividad(ISalida salida, TreeMap<String, Instancia> notas) throws Exception;

	/**
	 * @param instancia
	 * @param temasNotaContableAux
	 * @param totalesNotaContableAux
	 * @param codigoUsuario
	 * @param aprobada
	 * @param codigoCausalDevolucion
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public int siguienteActividad(Instancia instancia, Collection<NotaContableTema> temasNotaContableAux, Collection<NotaContableTotal> totalesNotaContableAux, int codigoUsuario, boolean aprobada, int codigoCausalDevolucion, String otraCausalDev)
			throws Exception, java.rmi.RemoteException;

	/**
	 * @param palabraClave
	 * @param estado
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Concepto> searchConcepto(String palabraClave, String estado) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public FechaHabilitada getFechaHabilitadaPorSucursal(FechaHabilitada row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<Instancia> getInstanciasPorEstadoYSucursalOrigenYUsuario(Instancia row) throws Exception;

	/**
	 * @param estado
	 * @param codigoSucursal
	 * @param codigoRol
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<UsuarioInstancias> getInstanciasPorEstadoYSucursalOrigenYRol(String estado, String codigoSucursal, int codigoRol) throws Exception;

	/**
	 * @param estado
	 * @param codigoSucursal
	 * @param codigoRol
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public UsuarioModulo getUsuarioAsignadoPorBalanceo(String estado, String codigoSucursal, int codigoRol) throws Exception;

	/**
	 * @param codigoNotaContable
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<NotaContableCrucePartidaPendiente> getCrucesPartidasPendientesPorNotaContable(int codigoNotaContable) throws Exception;

	/**
	 * @param row
	 * @param temasNotaContable
	 * @param totalesNotaContable
	 * @param codigoUsuario
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public int updateNotaContableRegistro(NotaContable row, Collection<NotaContableTema> temasNotaContable, Collection<NotaContableTotal> totalesNotaContable, int codigoUsuario) throws Exception;

	public int updateNotaContableLibre(NotaContable nota, List<Anexo> anexos, List<NotaContableRegistroLibre> temasNotaContable, int codUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public NotaContable getNotaContable(NotaContable row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public NotaContable getNotaContablePorNumeroRadicacion(NotaContable row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<NotaContable> getNotaContablesPorEstado(NotaContable row) throws Exception;

	/**
	 * @param estadoInstancia
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<NotaContable> getNotaContablesPorEstadoInstancia(String estadoInstancia) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<NotaContable> getNotaContables() throws Exception;

	public Collection<NotaContable> getNotaContablesPrecierreCierre(boolean esPrecierre) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addMontoMaximo(MontoMaximo row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateMontoMaximo(MontoMaximo row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteMontoMaximo(MontoMaximo row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoMontoMaximo(MontoMaximo row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public MontoMaximo getMontoMaximo(MontoMaximo row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<MontoMaximo> getMontoMaximosPorEstado(MontoMaximo row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<MontoMaximo> getMontoMaximos() throws Exception;

	/**
	 * @param codigoUsuario
	 * @param operacion
	 * @param tipoRegistro
	 * @param codigoRegistro
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addRegistroAuditoriaIngreso(int codigoUsuario, String operacion, String tipoRegistro, String codigoRegistro) throws Exception;

	public int addRegistroAuditoria(Connection con, int codigoUsuario, String operacion, String tipoRegistro, String codigoRegistro) throws Exception;

	/**
	 * @param codigoSucursalOrigen
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public String verificarUsuarioSubGerente(String codigoSucursalOrigen) throws Exception;

	/**
	 * @param codigoSucursalOrigen
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public String verificarCentroEspecial(String codigoSucursalOrigen) throws Exception;

	/**
	 * @return
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws CreateException
	 * @throws Exception
	 */
	public int getDiasHabilesDesdeUltimoCierre() throws RemoteException, NamingException, CreateException, Exception;

	/**
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void reasignarMisPendientes(Connection con, int codigoUsuario, int codUsuarioLogueado) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void addMontoAutorizadoGeneral(MontoAutorizadoGeneral row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void updateMontoAutorizadoGeneral(MontoAutorizadoGeneral row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void deleteMontoAutorizadoGeneral(MontoAutorizadoGeneral row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public void changeEstadoMontoAutorizadoGeneral(MontoAutorizadoGeneral row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public MontoAutorizadoGeneral getMontoAutorizadoGeneral(MontoAutorizadoGeneral row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<MontoAutorizadoGeneral> getMontosAutorizadosGeneralesPorEstado(MontoAutorizadoGeneral row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<MontoAutorizadoGeneral> getMontosAutorizadosGenerales() throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<MontoAutorizadoGeneral> searchMontoAutorizadoGeneral(String palabraClave) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public UnidadAnalisis getUnidadAnalisisPorAutorizaReferenciaCruceYEstado(UnidadAnalisis row) throws Exception;

	/**
	 * @param partidasSeleccionadas
	 * @param partidaPendiente
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public boolean searchPartida(ArrayList<PartidaPendiente> partidasSeleccionadas, PartidaPendiente partidaPendiente) throws java.rmi.RemoteException;

	/**
	 * @param codigoNotaContable
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public Collection<NotaContableRegistroLibre> getRegistrosLibresPorNotaContable(int codigoNotaContable) throws Exception;

	/**
	 * @param codigoNotaContable
	 * @param codigo
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public NotaContableRegistroLibre getRegistroLibreNotaContablePorNotaContableYCodigo(int codigoNotaContable, int codigo) throws Exception;

	/**
	 * @param notaContable
	 * @param temasNotaContable
	 * @param registrosLibresNotaContable
	 * @param anexosNotaContable
	 * @param totalesNotaContable
	 * @param partidasPendientes
	 * @param codigoUsuario
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws CreateException
	 * @throws java.rmi.RemoteException
	 */
	public int crearInstanciaNotaContable(NotaContable notaContable, Collection<NotaContableTema> temasNotaContable, Collection<NotaContableRegistroLibre> registrosLibresNotaContable, Collection<Anexo> anexosNotaContable,
			Collection<NotaContableTotal> totalesNotaContable, Collection<PartidaPendiente> partidasPendientes, int codigoUsuario) throws Exception, NamingException, CreateException, RemoteException, java.rmi.RemoteException;

	/**
	 * @param row
	 * @param montoNotaContable
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public MontoAutorizadoGeneral getMontoAutorizadoGeneralPorTemaAutorizacionYTipoEventoNotaContableYEstado(MontoAutorizadoGeneral row, double montoNotaContable) throws Exception;

	/**
	 * @param row
	 * @param montoNotaContable
	 * @return
	 * @throws Exception
	 * @throws java.rmi.RemoteException
	 */
	public int getPosicionMontoAutorizadoGeneralPorTemaAutorizacionYTipoEventoNotaContableYEstado(MontoAutorizadoGeneral row, double montoNotaContable) throws Exception;
	
	public Collection<Instancia> getInstanciasPorNotaContable(NotaContable notaContable, String codSucUsuario, int codRol) throws Exception;

	public Collection<Instancia> getInstanciasPorNumeroRadicacion(NotaContable notaContable, String codSucUsuario, int codRol) throws Exception;

	public Collection<Instancia> getInstanciasPorAsientoContableAndFecha(NotaContable notaContable, String codSucUsuario, int codRol) throws Exception;

	public Collection<SubMenuRol> findSubMenuRolByRol(Rol rol) throws Exception, RemoteException;

	public <T extends CommonVO<T>> Map<String, String> getCV(Class<T> clazz) throws Exception, RemoteException;

	public <T extends CommonVO<T>> Map<String, String> getCVBy(Class<T> clazz, String filtro) throws Exception, RemoteException;

	public Map<?, String> getCVEntesAut() throws Exception;

	TreeMap<Menu, TreeSet<SubMenu>> getMenu(int codigo) throws RemoteException, NamingException, CreateException, Exception, RemoteException;

	public Collection<String> getInfoGeneralArchAltamira(Integer notaContableCodigo) throws Exception, RemoteException;

	public Collection<String> getInfoCruceArchAltamira(Integer notaContableCodigo) throws Exception, RemoteException;

	public Collection<String> getInfoLibreArchAltamira(Integer notaContableCodigo) throws Exception, RemoteException;

	public Collection<RechazoSalida> getRechazoSalidaByNotaContable(String numRadicacion) throws Exception, RemoteException;

	public void addRechazoSalida(RechazoSalida rechazoSalida, int codigoUsuario) throws Exception, RemoteException;

	public Collection<NotaContableRegistroLibre> getRegistrosNotaContableLibre(Number codigoNotaContable) throws Exception;

	public int getUltimoUsuarioActividadRealizadaPorRadicacion(String numRadicacion) throws Exception;

	public Collection<Anexo> getAnexosPorInstancia(int codInstanncia) throws Exception;

	public void updateNotaContable(Connection con, NotaContable nota, int codigoUsuario) throws Exception;

	public Collection<Instancia> getInstanciasPor(String codigoSucursalOrigen, String codigoSucursalDestino, Integer codigoConcepto, Integer codigoTema, Integer codigoTipoEvento, String partidaContable, String numeroIdentificacion, Date fechaDesde,
			Date fechaHasta, String codigoDivisa, String codigoEstado, String descripcion, String codSucUsuario, int codRol) throws Exception;

	public Collection<Instancia> getInstanciasRegLibrePor(String sucOrigen, String sucDestino, String partida, String numIdentificacion, Date desde, Date hasta, String divisa, String estado, String descripcion, String codSucUsuario, int codRol)
			throws Exception;

	public Collection<Instancia> getInstanciasCruceRefPor(String sucOrigen, String sucDestino, String partida, Date desde, Date hasta, String divisa, String estado, String codSucUsuario, int codRol) throws Exception;

	public void updateParametro(Parametro registro, int codigoUsuario) throws Exception;

	public void updateParametros(Collection<Parametro> registros, int codigoUsuario) throws Exception;

	public Collection<Parametro> getParametros() throws Exception;

	public void updateFechaNotaContableTema(NotaContableTema t, int codigoUsuario) throws Exception;

	public void updateFechaNotaContableCruceRef(NotaContableCrucePartidaPendiente t, int codigoUsuario) throws Exception;

	public void updateFechaNotaContableRegLibre(NotaContableRegistroLibre t, int codigoUsuario) throws Exception;

	public ArrayList<NotaContableTotal> getDatosDeInstancias(Collection<Instancia> instancias, boolean totales) throws Exception;

	public void sendMail(Instancia instancia, UsuarioLogueado usuarioLogueado) throws Exception;

	public void processSendMail(List<Instancia> instancias, UsuarioLogueado usuarioLogueado) throws Exception;

	public Collection<Instancia> getInstanciasPendientes() throws Exception;

	public Collection<ActividadRealizada> getActividadesParaReporteTiempos(Integer codUsuario) throws Exception;

	public List<UsuarioModulo> getUsuariosAltamiraInactivos() throws Exception;

	public List<Tema> getTemasCuentasInactivas() throws Exception;

	public Collection<UsuarioModulo> getTiemposPorUsuario(Timestamp desde) throws Exception;

}
