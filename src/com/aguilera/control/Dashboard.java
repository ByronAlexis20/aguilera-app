package com.aguilera.control;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.aguilera.modelo.Cabecera;
import com.aguilera.modelo.Categoria;
import com.aguilera.modelo.Cita;
import com.aguilera.modelo.Detalle;
import com.aguilera.modelo.DisenioCategoria;
import com.aguilera.modelo.Medida;
import com.aguilera.modelo.Producto;
import com.aguilera.modeloDAO.CabeceraDAO;
import com.aguilera.modeloDAO.CategoriaDAO;
import com.aguilera.modeloDAO.CitaDAO;
import com.aguilera.modeloDAO.DetalleDAO;
import com.aguilera.modeloDAO.DisenioCategoriaDAO;
import com.aguilera.modeloDAO.MedidaDAO;
import com.aguilera.modeloDAO.NotaCabeceraDAO;
import com.aguilera.modeloDAO.ProductoDAO;

public class Dashboard {
	List<Mes> listaMes;
	Mes mesSeleccionado;
	@Wire private Textbox txtAnio;
	@Wire private Image imGraficaVentas;
	@Wire private Label lblVentas;
	
	@Wire private Image imPorcentajeVentaCategoria;
	@Wire private Label lblPastelPorcentajeCategoria;
	
	@Wire private Image imGraficaVentasTalla;
	@Wire private Label lblVentasTalla;
	
	@Wire private Image imGraficaCitas;
	@Wire private Label lblCitas;
	
	NotaCabeceraDAO notaCabeeraDAO = new NotaCabeceraDAO();
	CabeceraDAO cabeceraDAO = new CabeceraDAO();
	CategoriaDAO categoriaDAO = new CategoriaDAO();
	DetalleDAO detalleDAO = new DetalleDAO();
	DisenioCategoriaDAO disenioCatDAO = new DisenioCategoriaDAO();
	MedidaDAO medidaDAO = new MedidaDAO();
	ProductoDAO productoDAO = new ProductoDAO();
	CitaDAO citaDAO = new CitaDAO();
	
	List<Producto> listaProductos;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException {
		Selectors.wireComponents(view, this, false);
		this.cargarListadoMeses();
		
		Date date = new Date();
        ZoneId timeZone = ZoneId.systemDefault();
        LocalDate getLocalDate = date.toInstant().atZone(timeZone).toLocalDate();
		txtAnio.setValue(String.valueOf(getLocalDate.getYear()));
		this.cargarGraficoVentas();
		this.cargarGraficoPastel();
		this.cargarGraficoVentasPorTalla();
		this.cargartablaInventario();
		this.cargarGraficaCitas();
	}
	private void cargartablaInventario() {
		try {
			listaProductos = this.productoDAO.buscarPorTexto("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarGraficaCitas() {
		try {
			List<Cita> listaCitas = this.citaDAO.findAll();
			Integer contPendiente = 0; //P
			Integer contPerdida = 0; //L
			Integer contEjecutada = 0; //E
			for(Cita ct : listaCitas) {
				ZoneId timeZone = ZoneId.systemDefault();
		        LocalDate getLocalDate = ct.getFecha().toInstant().atZone(timeZone).toLocalDate();
		        if(getLocalDate.getYear() == Integer.parseInt(txtAnio.getText()) && getLocalDate.getMonthValue() == mesSeleccionado.getIdMes()) {
		        	if(ct.getEstadoCita().equals("P")) {
		        		contPendiente = contPendiente + 1;
		        	}else if(ct.getEstadoCita().equals("L")) {
		        		contPerdida = contPerdida + 1; 
		        	}else if(ct.getEstadoCita().equals("E")) {
		        		contEjecutada = contEjecutada + 1;
		        	}
		        }
			}
			lblCitas.setValue("Citas del mes " + mesSeleccionado.getMes() + " del año " + txtAnio.getText());
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			dataset.addValue(contPendiente, "Citas", "Pendientes");
			dataset.addValue(contPerdida, "Citas", "Perdidas");
			dataset.addValue(contEjecutada, "Citas", "Ejecutadas");
			
			JFreeChart chart = ChartFactory.createBarChart("Citas", null, null, dataset, PlotOrientation.HORIZONTAL, true, true, false);
			chart.setBackgroundPaint( Color.white );
			
			CategoryPlot plot = (CategoryPlot) chart.getPlot();
			//fondo del grafico
			plot.setBackgroundPaint( Color.WHITE );
			//lineas divisoras
			plot.setDomainGridlinesVisible( true );
	        plot.setRangeGridlinePaint( Color.BLACK );
			
	        BarRenderer renderer = (BarRenderer) plot.getRenderer();
	        renderer.setDrawBarOutline(false);
	        
	        //Dar color a ada barra
	        GradientPaint gp0= new GradientPaint(0.0f,0.0f,Color.green,0.0f,0.0f,new Color(78,213,15));
	        
	        renderer.setSeriesPaint(0,gp0);

	        CategoryAxis domainAxis = plot.getDomainAxis();
	        domainAxis.setCategoryLabelPositions(
	                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI/4.0));
			
			BufferedImage bi = chart.createBufferedImage(700, 350, BufferedImage.SCALE_REPLICATE , null);
			byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);
			AImage imagen = new AImage("Citas", bytes);
			
			imGraficaCitas.setContent(imagen);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarGraficoVentasPorTalla() throws IOException {
		//se declara el grafico XY Lineal
        XYDataset xydataset = xyDataset();
        JFreeChart jfreechart = ChartFactory.createXYLineChart(
                "" , "", "Total de ventas por talla",  
                xydataset, PlotOrientation.VERTICAL,  true, true, false);
        
        //personalización del grafico
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        xyplot.setBackgroundPaint( Color.white );
        xyplot.setDomainGridlinePaint( Color.BLACK );
        xyplot.setRangeGridlinePaint( Color.BLACK );
        xyplot.setForegroundAlpha(0.9f);
        // -> Pinta Shapes en los puntos dados por el XYDataset
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
        xylineandshaperenderer.setBaseShapesVisible(true);
        //--> muestra los valores de cada punto XY
        XYItemLabelGenerator xy = new StandardXYItemLabelGenerator();
        xylineandshaperenderer.setBaseItemLabelGenerator( xy );
        xylineandshaperenderer.setBaseItemLabelsVisible(true);
        xylineandshaperenderer.setBaseLinesVisible(true);
        xylineandshaperenderer.setBaseItemLabelsVisible(true);                
        //fin de personalización

        //se crea la imagen y se asigna a la clase ImageIcon
        BufferedImage bi = jfreechart.createBufferedImage(500, 250, BufferedImage.SCALE_REPLICATE , null);
		byte[] bytes = EncoderUtil.encode(bi, ImageFormat.JPEG, true);
		AImage image = new AImage("Pie Chart", bytes);
		imGraficaVentasTalla.setContent(image);
	}
	private XYDataset xyDataset() {
		List<Cabecera> listaCabecera = this.cabeceraDAO.findAll();
		List<Integer> tallasCamisa = new ArrayList<>();
		List<Integer> tallasPantalon = new ArrayList<>();
		boolean bandera = false;
		for(Cabecera cab : listaCabecera) {
			ZoneId timeZone = ZoneId.systemDefault();
	        LocalDate getLocalDate = cab.getFechaVenta().toInstant().atZone(timeZone).toLocalDate();
	        if(getLocalDate.getYear() == Integer.parseInt(txtAnio.getText())) {
	        	List<Medida> listaMedidas = this.medidaDAO.buscarPorPedido(cab.getPedido().getId());
	        	for(Medida med : listaMedidas) {
	        		bandera = false;
	        		if(med.getTallaPantalon() != null) {
	        			for(Integer in : tallasPantalon) {
	        				if(in == med.getTallaPantalon()) {
	        					bandera = true;
	        				}
	        			}
	        			if(bandera == false) {
		        			tallasPantalon.add(med.getTallaPantalon());
		        		}
	        		}
	        		
	        		bandera = false;
	        		if(med.getTallaCamisa() != null) {
	        			for(Integer in : tallasCamisa) {
	        				if(in == med.getTallaCamisa()) {
	        					bandera = true;
	        				}
	        			}
	        			if(bandera == false) {
		        			tallasCamisa.add(med.getTallaCamisa());
		        		}
	        		}
	        	}
	        }
		}
		XYSeries sCamisas = new XYSeries("Camisa");
        XYSeries sPantalones = new XYSeries("Pantalón");
        
        List<Integer> todasTallas = new ArrayList<>();
        for(Integer i : tallasCamisa) {
        	todasTallas.add(i);
        }
        for(Integer i : tallasPantalon) {
        	todasTallas.add(i);
        }
		//una vez q se obtienen las tallas ahora se hacen los contadores por las tallas
        for(Integer tt : todasTallas) {
        	Integer contador = 0;
        	for(Integer tp : tallasPantalon) {
        		if(tp == tt) {
        			for(Cabecera cab : listaCabecera) {
            			ZoneId timeZone = ZoneId.systemDefault();
            	        LocalDate getLocalDate = cab.getFechaVenta().toInstant().atZone(timeZone).toLocalDate();
            	        if(getLocalDate.getYear() == Integer.parseInt(txtAnio.getText())) {
            	        	List<Medida> listaMedidas = this.medidaDAO.buscarPorPedido(cab.getPedido().getId());
            	        	for(Medida med : listaMedidas) {
            	        		if(med.getTallaPantalon() != null) {
            	        			if(med.getTallaPantalon() == tp) {
            	        				contador = contador + med.getCantidad();
            	        			}
            	        		}
            	        	}
            	        }
            		}
        		}
        	}
        	sPantalones.add( tt, contador);
		}
        
        for(Integer tt : tallasCamisa) {
        	Integer contador = 0;
        	for(Integer tp : tallasCamisa) {
        		if(tp == tt) {
        			for(Cabecera cab : listaCabecera) {
            			ZoneId timeZone = ZoneId.systemDefault();
            	        LocalDate getLocalDate = cab.getFechaVenta().toInstant().atZone(timeZone).toLocalDate();
            	        if(getLocalDate.getYear() == Integer.parseInt(txtAnio.getText())) {
            	        	List<Medida> listaMedidas = this.medidaDAO.buscarPorPedido(cab.getPedido().getId());
            	        	for(Medida med : listaMedidas) {
            	        		if(med.getTallaCamisa() != null) {
            	        			if(med.getTallaCamisa() == tp) {
            	        				contador = contador + med.getCantidad();
            	        			}
            	        		}
            	        	}
            	        }
            		}
        		}
        	}
        	sCamisas.add( tt, contador);
		}
        
        XYSeriesCollection xyseriescollection =  new XYSeriesCollection();
        xyseriescollection.addSeries( sPantalones );        
        xyseriescollection.addSeries( sCamisas );

        return xyseriescollection;
	}
	@Command
	public void actualizar() throws IOException{
		if(mesSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un mes");
			return;
		}
		if(txtAnio.getText().isEmpty()) {
			Clients.showNotification("Debe registrar un año");
			return;
		}
		this.cargarGraficoVentas();
		this.cargarGraficoPastel();
		this.cargarGraficoVentasPorTalla();
		this.cargarGraficaCitas();
	}
	private void cargarGraficoPastel() {
		try {
			List<GraficoPastel> datosGraficoPastel = new ArrayList<>();
			List<Categoria> listaCategoria = this.categoriaDAO.findAll();
			List<Detalle> detalles = this.detalleDAO.findAll();
			for(Categoria cat : listaCategoria) {
				Integer cantidad = 0;
				for(Detalle det : detalles) {
					ZoneId timeZone = ZoneId.systemDefault();
			        LocalDate getLocalDate = det.getCabecera().getFechaVenta().toInstant().atZone(timeZone).toLocalDate();
			        if(getLocalDate.getYear() == Integer.parseInt(txtAnio.getText())) {
			        	List<DisenioCategoria> listaDisenioCat = this.disenioCatDAO.buscarPorCategoria(cat.getId(), det.getDisenio().getId());
			        	if(listaDisenioCat.size() > 0) {
			        		cantidad = cantidad + det.getCantidad();
			        	}
			        }
				}
				GraficoPastel gp = new GraficoPastel();
				gp.setCantidad(cantidad);
				gp.setCategoria(cat);
				datosGraficoPastel.add(gp);
			}
			//realizar la grafica*****************************************************************************************
			DefaultPieDataset pieDataset = new DefaultPieDataset();
			Integer totalCantidad = 0;
			for(GraficoPastel gp : datosGraficoPastel) {
				totalCantidad = totalCantidad + gp.getCantidad();
			}
			if(totalCantidad > 0) {
				lblPastelPorcentajeCategoria.setValue("Porcentaje de ventas por categoria del mes de " + mesSeleccionado.getMes() + " del año " + txtAnio.getText());
				for(GraficoPastel gp : datosGraficoPastel) {
					double total = (gp.getCantidad() * 100) / totalCantidad;
					pieDataset.setValue(gp.getCategoria().getNombre() + " (" + gp.getCantidad() + ")", total);
				}
				
				pieDataset.setNotify(true);
				JFreeChart chart = ChartFactory.createPieChart3D("Categorias", pieDataset,true,true,false);
				PiePlot3D plot = (PiePlot3D) chart.getPlot();
				plot.setBackgroundPaint( Color.WHITE );
				plot.setForegroundAlpha(0.9f);
				BufferedImage bi = chart.createBufferedImage(500, 250, BufferedImage.SCALE_REPLICATE , null);
				byte[] bytes = EncoderUtil.encode(bi, ImageFormat.JPEG, true);
				AImage image = new AImage("Pie Chart", bytes);
				imPorcentajeVentaCategoria.setContent(image);
			}
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarGraficoVentas() {
		try {
			
			List<Cabecera> lista = this.cabeceraDAO.findAll();
			List<Cabecera> listaVentas = new ArrayList<>();
			for(Cabecera nt : lista) {
				ZoneId timeZone = ZoneId.systemDefault();
		        LocalDate getLocalDate = nt.getFechaVenta().toInstant().atZone(timeZone).toLocalDate();
		        if(getLocalDate.getYear() == Integer.parseInt(txtAnio.getText()) ) {
		        	listaVentas.add(nt);
		        }
			}
			
			Double enero = 0.0, febrero = 0.0, marzo = 0.0, abril = 0.0, mayo = 0.0, junio = 0.0, julio = 0.0, agosto = 0.0,
					septiembre = 0.0, octubre = 0.0, noviembre = 0.0, diciembre = 0.0;
			for(Cabecera nt : listaVentas) {
				ZoneId timeZone = ZoneId.systemDefault();
		        LocalDate getLocalDate = nt.getFechaVenta().toInstant().atZone(timeZone).toLocalDate();
		        switch(getLocalDate.getMonthValue())
		        {
		        	case 1:
		        		enero = enero + nt.getPedido().getTotal();
		        		break;
		        	case 2:
		        		febrero = febrero + nt.getPedido().getTotal();
		        		break;
		        	case 3:
		        		marzo = marzo + nt.getPedido().getTotal();
		        		break;
		        	case 4:
		        		abril = abril + nt.getPedido().getTotal();
		        		break;
		        	case 5:
		        		mayo = mayo + nt.getPedido().getTotal();
		        		break;
		        	case 6:
		        		junio = junio + nt.getPedido().getTotal();
		        		break;
		        	case 7:
		        		julio = julio + nt.getPedido().getTotal();
		        		break;
		        	case 8:
		        		agosto = agosto + nt.getPedido().getTotal();
		        		break;
		        	case 9:
		        		septiembre = septiembre + nt.getPedido().getTotal();
		        		break;
		        	case 10:
		        		octubre = octubre + nt.getPedido().getTotal();
		        		break;
		        	case 11:
		        		noviembre = noviembre + nt.getPedido().getTotal();
		        		break;
		        	case 12:
		        		diciembre = diciembre + nt.getPedido().getTotal();
		        		break;
		        }
			}
			lblVentas.setValue("Ventas del año " + txtAnio.getText());
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			dataset.addValue(enero, "Ingresos", "Enero");
			dataset.addValue(febrero, "Ingresos", "Febrero");
			dataset.addValue(marzo, "Ingresos", "Marzo");
			dataset.addValue(abril, "Ingresos", "Abril");
			dataset.addValue(mayo, "Ingresos", "Mayo");
			dataset.addValue(junio, "Ingresos", "Junio");
			dataset.addValue(julio, "Ingresos", "Julio");
			dataset.addValue(agosto, "Ingresos", "Agosto");
			dataset.addValue(septiembre, "Ingresos", "Septiembre");
			dataset.addValue(octubre, "Ingresos", "Octubre");
			dataset.addValue(noviembre, "Ingresos", "Noviembre");
			dataset.addValue(diciembre, "Ingresos", "Diciembre");
			
			JFreeChart chart = ChartFactory.createBarChart("Ingresos anuales", null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
			chart.setBackgroundPaint( Color.white );
			
			CategoryPlot plot = (CategoryPlot) chart.getPlot();
			//fondo del grafico
			plot.setBackgroundPaint( Color.WHITE );
			//lineas divisoras
			plot.setDomainGridlinesVisible( true );
	        plot.setRangeGridlinePaint( Color.BLACK );
			
	        BarRenderer renderer = (BarRenderer) plot.getRenderer();
	        renderer.setDrawBarOutline(false);
	        
	        //Dar color a ada barra
	        GradientPaint gp0= new GradientPaint(0.0f,0.0f,new Color(15,129,213),0.0f,0.0f,new Color(14,108,177));
	        
	        renderer.setSeriesPaint(0,gp0);

	        CategoryAxis domainAxis = plot.getDomainAxis();
	        domainAxis.setCategoryLabelPositions(
	                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI/4.0));
			
			BufferedImage bi = chart.createBufferedImage(700, 350, BufferedImage.SCALE_REPLICATE , null);
			byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);
			AImage imagen = new AImage("Ventas", bytes);
			
			imGraficaVentas.setContent(imagen);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void cargarListadoMeses() {
		try {
			List<Mes> lista = new ArrayList<>();
			Mes mes1 = new Mes(1, "Enero");
			lista.add(mes1);
			Mes mes2 = new Mes(2, "Febrero");
			lista.add(mes2);
			Mes mes3 = new Mes(3, "Marzo");
			lista.add(mes3);
			Mes mes4 = new Mes(4, "Abril");
			lista.add(mes4);
			Mes mes5 = new Mes(5, "Mayo");
			lista.add(mes5);
			Mes mes6 = new Mes(6, "Junio");
			lista.add(mes6);
			Mes mes7 = new Mes(7, "Julio");
			lista.add(mes7);
			Mes mes8 = new Mes(8, "Agosto");
			lista.add(mes8);
			Mes mes9 = new Mes(9, "Septiembre");
			lista.add(mes9);
			Mes mes10 = new Mes(10, "Octubre");
			lista.add(mes10);
			Mes mes11 = new Mes(11, "Noviembre");
			lista.add(mes11);
			Mes mes12 = new Mes(12, "Diciembre");
			lista.add(mes12);
			listaMes = lista;
			
			Date date = new Date();
	        ZoneId timeZone = ZoneId.systemDefault();
	        LocalDate getLocalDate = date.toInstant().atZone(timeZone).toLocalDate();
			
			for(Mes m : lista) {
				if(m.getIdMes() == getLocalDate.getMonthValue()) {
					mesSeleccionado = m;
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public List<Producto> getListaProductos() {
		return listaProductos;
	}
	public void setListaProductos(List<Producto> listaProductos) {
		this.listaProductos = listaProductos;
	}
	public List<Mes> getListaMes() {
		return listaMes;
	}

	public void setListaMes(List<Mes> listaMes) {
		this.listaMes = listaMes;
	}

	public Mes getMesSeleccionado() {
		return mesSeleccionado;
	}

	public void setMesSeleccionado(Mes mesSeleccionado) {
		this.mesSeleccionado = mesSeleccionado;
	}

	public class Mes {
		private Integer idMes;
		private String mes;
		public Mes(Integer idMes, String mes) {
			super();
			this.idMes = idMes;
			this.mes = mes;
		}
		public Integer getIdMes() {
			return idMes;
		}
		public void setIdMes(Integer idMes) {
			this.idMes = idMes;
		}
		public String getMes() {
			return mes;
		}
		public void setMes(String mes) {
			this.mes = mes;
		}
	}
	
	public class GraficoPastel {
		private Categoria categoria;
		private Integer cantidad;
		public GraficoPastel() {
			super();
		}
		public GraficoPastel(Categoria categoria, Integer cantidad) {
			super();
			this.categoria = categoria;
			this.cantidad = cantidad;
		}
		public Categoria getCategoria() {
			return categoria;
		}
		public void setCategoria(Categoria categoria) {
			this.categoria = categoria;
		}
		public Integer getCantidad() {
			return cantidad;
		}
		public void setCantidad(Integer cantidad) {
			this.cantidad = cantidad;
		}
	}
}
