
package control;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import core.sakai.objects.SakaiAssignment.SakaiAssignmentContent;
import core.sakai.objects.SakaiSite;

public class TestClass 
{
	public static void example() throws ParserConfigurationException, SAXException, IOException, JAXBException
	{
		LoginControl.login("admin", "admin");
		HashMap<String, SakaiSite> sites = Sites.getAllSites();
		
		for(String str : sites.keySet())
		{
			System.out.println(str);
		}
		
		SakaiSite mercury = sites.get("mercury site");
		HashMap<String, SakaiAssignmentContent> assignments = 
				mercury.getAllAssignments();
		
		for(String str : assignments.keySet())
		{
			System.out.println(str);
		}
		
		System.out.println(assignments.get("这个是测试性的作业").getInstructions());
		
		Sites.addSitesAddHandler
		(
			new SitesAdd()
			{
				@Override
				public void siteAdd(SakaiSite newSite) 
				{
					System.out.println("A new Site is added:" + newSite.getTitle());
				}
			}
		);
		
		mercury.addAssignmentAddHandler
		(
			new AssignmentAdd()
			{

				@Override
				public void newAssignment(SakaiAssignmentContent assignment) 
				{
					System.out.println("new ass add:" + assignment.getTitle());
				}
				
			}
		);
		
		Sites.saveInfo();
		
	}
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, JAXBException
	{
		example();
	}
	
}
