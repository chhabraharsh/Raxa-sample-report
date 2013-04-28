import org.eclipse.birt.report.engine.*;
import org.eclipse.birt.report.engine.api.*;
import org.eclipse.core.internal.registry.RegistryProviderFactory;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
public class ReportPublish {
	LoadDriver t= null;
	Connection c;
	String extract, insert;
	Calendar ct;
	Date created_on;
	Statement s;
	PreparedStatement ps;
	ResultSet r;
	int uid;
	String name, email, grn, rn, grn1;
	String location = "C:/harsh/WorkSpace/SampleReport";


	public static void main(String [] args)
	{
	ReportPublish r= new ReportPublish();
	try {
		r.convert();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
	public void convert() throws SQLException
	{
		t= new LoadDriver();
		c= t.getConnection();
		extract= "select nid,field_first_name_value, field_email_id_value from content_type_student";
		EngineConfig config= new EngineConfig();
		try {
			Platform.startup( config );
		} catch (BirtException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  //If using RE API in Eclipse/RCP application this is not needed.
		IReportEngineFactory factory = (IReportEngineFactory) Platform
				.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
		IReportEngine engine = factory.createReportEngine( config );
		IReportRunnable design;

		//Open the report design


		//Create task to run and render the report,

	try {
			s= c.createStatement();
			r= s.executeQuery(extract);


		    while(r.next())
			{
			design = engine.openReportDesign("C:/harsh/WorkSpace/SampleReport/ReportDemo/barChart.rptdesign");
				IRunAndRenderTask task = engine.createRunAndRenderTask(design);
				uid= r.getInt("nid");
				name= r.getString("field_first_name_value");
				email= r.getString("field_email_id_value");
				ct= Calendar.getInstance();
				created_on= new Date(ct.getTimeInMillis());

				//Set parent classloader for engine
			if(name!=null&& email!=null)
				{
					grn= "WEEKLY"+"PROGRESS" + name + uid+ created_on;
					grn1= "WEEKLY"+"PROGRESS" + name + uid;

				task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, ReportPublish.class.getClassLoader());
				task.setParameterValue("uid", uid);
				task.validateParameters();
				PDFRenderOption option= new PDFRenderOption();
				option.setOutputFormat("pdf");
				option.setOutputFileName("C:/harsh/WorkSpace/SampleReport/" + grn1 +".pdf");
				task.setRenderOption(option);
				//run and render report
				task.run();

				task.close();


				insert= "insert into rpt_report_generated_details (uid, user_name, email_id, frequency, type, generated_report_name, report_name, file_location, created_on, status) values(?,?,?,?,?,?,?,?,?,?)";

				ps= c.prepareStatement(insert);

				rn= "WEEKLY"+"PROGRESS" + name +  created_on;

                ps.setInt(1, uid);

				ps.setString(2, name);
				ps.setString(3, email);
				ps.setString(4, "WEEKLY");
				ps.setString(5, "PROGRESS");
				ps.setString(6,grn );
				ps.setString(7, rn );
				ps.setString(8, location );
				ps.setTimestamp(9, new java.sql.Timestamp(created_on.getTime()));
				ps.setString(10, "PENDING");
				ps.executeUpdate();

				}
			}



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		catch (EngineException ex)
		{
			ex.printStackTrace();
		}
	}

}
