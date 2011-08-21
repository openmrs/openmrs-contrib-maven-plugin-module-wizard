package org.openmrs.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.archetype.ArchetypeManager;
import org.apache.maven.archetype.ui.ArchetypeSelector;
import org.apache.maven.archetype.ui.ArchetypeGenerationConfigurator;
import org.apache.maven.archetype.generator.ArchetypeGenerator;
import org.apache.maven.shared.invoker.Invoker;

import org.codehaus.plexus.components.interactivity.Prompter;
import org.apache.maven.archetype.mojos.CreateProjectFromArchetypeMojo;



import java.io.File;
import java.util.List;
import java.util.ArrayList;


import java.util.Properties;
import java.util.regex.Pattern;
import java.lang.reflect.Field;

/**
 * Prompts and executes archetypes.
 * Such as a complete archetype followed by partial archetypes.
 * Uses the Archetype Mojo from maven-archetype-plugin.
 * 
 * @goal generate
 * @requiresProject false
 * @execute phase="generate-sources"
 * @author mblanchette
 * @author goutham
 */
public class WizardMojo extends CreateProjectFromArchetypeMojo {

    /**
     * The Plexus CommandLine prompt component.
     *
     * @component
     * @required
     */
    private Prompter prompter;

    /** @component */
    private ArchetypeManager archetype;

    /** @component */
    private ArchetypeSelector selector;

    /** @component */
    ArchetypeGenerationConfigurator configurator;

    /** @component */
    ArchetypeGenerator generator;

    /** @component */
    private Invoker invoker;

    /**
     * The archetype's artifactId.
     * This can be an ordered comma separated list.
     *
     * @parameter expression="${archetypeArtifactId}" default-value="openmrs-archetype-basicmodule-creation"
     */
    private String archetypeArtifactId;

    /**
     * The archetype's groupId.
     *
     * @parameter expression="${archetypeGroupId}" default-value="org.openmrs.maven.archetypes"
     */
    private String archetypeGroupId;

    /**
     * The archetype's version.
     *
     * @parameter expression="${archetypeVersion}" default-value="1.0-SNAPSHOT"
     */
    private String archetypeVersion;

    /**
     * The archetype's repository.
     *
     * @parameter expression="${archetypeRepository}"
     */
    private String archetypeRepository;

    /**
     * The archetype's catalogs.
     * It is a comma separated list of catalogs.
     *
     * @parameter expression="${archetypeCatalog}" default-value="remote,local"
     */
    private String archetypeCatalog;

    /**
     * Local Maven repository.
     *
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    private ArtifactRepository localRepository;

    /**
     * List of remote repositories used by the resolver.
     *
     * @parameter  expression="${project.remoteArtifactRepositories}"
     * @readonly
     * @required
     */
    private List<ArtifactRepository> remoteArtifactRepositories;

    /**
     * User settings use to check the interactiveMode.
     *
     * @parameter expression="${interactiveMode}" default-value="true"
     * @required
     */
    private Boolean interactiveMode;

    /** @parameter expression="${basedir}" */
    private File basedir;

    /**
     *  @parameter expression="${session}"
     *  @readonly
     */
    private MavenSession session;

    /**
     * Additional goals that can be specified by the user during the creation of the archetype.
     *
     * @parameter expression="${goals}"
     */
    private String goals;

    /**
     * The generated project's artifactId.
     *
     * @parameter expression="${artifactId}" default-value="whirlygig"
     */
    private String artifactId;

    /**
     * The generated project's groupId.
     *
     * @parameter expression="${groupId}" default-value="org.openmrs.module"
     */
    private String groupId;

    /**
     * The generated project's version.
     *
     * @parameter expression="${version}" default-value="1.0-SNAPSHOT"
     */
    private String version;

    /**
     * The generated project's package name.
     *
     * @parameter  default-value="org.openmrs.module.whirlygig"
     */
    private String packageName;

    /**
     * The generated project's module name (no spaces).
     *
     * @parameter expression="${moduleNameNoSpaces}" default-value="whirlygigmodule"
     */
    private String moduleNameNoSpaces;

    /**
     * The generated project's module name.
     *
     * @parameter expression="${moduleName}" default-value="Whirlygig"
     */
    private String moduleName;

    /**
     * The generated project's module description.
     *
     * @parameter expression="${moduleDescription}" default-value="Allows user to generate and save whirlygigs."
     */
    private String moduleDescription;

    /**
     * The generated project's module author.
     *
     * @parameter expression="${user.name}" default-value="Goutham vvln"
     */
    private String moduleAuthor;

    /**
     * The generated project's openMRSVerison.
     *
     * @parameter expression="${openmrsVerison}" default-value="1.8.2"
     */
    
    private String openmrsVersion;
    
    
    /**
     * The generated project's admin page link.
     *
     * @parameter expression="${adminPageLink}" default-value="Manage Whirlygigs"
     */
    private String adminPageLink;
    
    
 
    /**
     * The generated project's object name
     * 
     * @parameter expression="${serviceDaoName}" default-value="Whirlygig" 
     */
    private String serviceDaoName;
    
         
    /**
     * The generated project's hibernate name.
     *
     * @parameter expression="${objectName}" default-value="Whirlygig"
     */
    private String objectName;
    
    
    /**
     * The generated project's admin link condition.
     *
     * @parameter  default-value="n"
     */
    private String adminLinkReply;
    
    /**
     * The generated project's SpringMvc page condition.
     *
     * @parameter  default-value="n"
     */
    private String springMvcReply;
    
    /**
     * The generated project's service/dao/hibernate condition.
     *
     * @parameter  default-value="n"
     */
    private String serviceReply;


    /**
     * The generated project's dependent modules condition.
     *
     * @parameter  expression="${dependentModules}" default-value="empty"  
     */
    private String dependentModules;
    
      
    public void execute() throws MojoExecutionException, MojoFailureException{
    
        if( Boolean.TRUE.equals( interactiveMode ) ) {
    	       
    	try{
    		
    		
    		getLog().info("----------------------------------------------------------------");
    		getLog().info("|                        Module Wizard                         |");
    		getLog().info("----------------------------------------------------------------");
    		
    		//loop for input parameters confirmation
    		String confirm="n";
        	while (!"y".equalsIgnoreCase(confirm)) {
         	
        	
        	/*wizard questions*/
        
        	//general parameters	
        	groupId=prompter.prompt("Group Id:",groupId);
    		artifactId=prompter.prompt("Artifact Id:",artifactId);
       		version=prompter.prompt("Version:",version);
    		moduleName=prompter.prompt("Module Name:",moduleName);
			moduleDescription=prompter.prompt("Module Description:",moduleDescription);
			moduleAuthor=prompter.prompt("Module Author:",moduleAuthor);
			openmrsVersion=prompter.prompt("OpenMRS Version Depended on:",openmrsVersion);
		
			//archetype selection questions and parameters based on reply of archetype selective questions
			adminLinkReply=prompter.prompt("Do you want admin page link :  (Y/N) ",adminLinkReply);
		    	if("y".equalsIgnoreCase(adminLinkReply))
			    	adminPageLink=prompter.prompt("Link Name:",adminPageLink);
    		
		    springMvcReply=prompter.prompt("Do you want Spring driven Mvc page :  (Y/N) ",springMvcReply);
    		
		    serviceReply=prompter.prompt("Do you want service/serviceimpl/dao/hibernatedao mapping :  (Y/N) ",serviceReply);
		    	if("y".equalsIgnoreCase(serviceReply))
		    	{
		    		serviceDaoName=prompter.prompt("Service Name:",serviceDaoName);
		    		objectName=prompter.prompt("Object Name:",objectName);
		    	}
		
		    if(prompter.prompt("Does this module depend on another module: (Y/N)").equalsIgnoreCase("y"))
		    {
		    	dependentModules="";
		    	dependentModules+=prompter.prompt("Module Id :")+':';
			    dependentModules+=prompter.prompt("Module version :")+',';
			    while(prompter.prompt("Does this module depend on another module: (Y/N)").equalsIgnoreCase("y"))	
			    {
			    	dependentModules+=prompter.prompt("Module Id :")+':';
			    	dependentModules+=prompter.prompt("Module version :")+',';		      
			    }
		    }else{
		    		dependentModules="empty";
		    }
		    
			confirm=prompter.prompt("Ready to create module. Are the above values correct? (Y/N)");
        	}    	
        	
    	} catch (Exception e) {
	   		throw new MojoExecutionException("Failed to prompt for wizard questions :", e);
		}
        }
    	
    	packageName="org.openmrs.module."+artifactId;
    	moduleNameNoSpaces.toLowerCase();
    	
    	
    	try {
    		setParameterWithOutSpaces("moduleNameNoSpaces",moduleName);
    	   	setParameterWithOutSpaces("serviceDaoName", serviceDaoName);
    	  	setParameterWithOutSpaces("objectName", objectName);
		} catch (Exception e) {
			throw new MojoExecutionException("Error in removing spaces "+e);
		}	
    	
		moduleName=moduleName+" Module";
				    	
    	    	  
    	//Adding properties to maven session so that they are available for archetype-mechanism
        Properties properties = new Properties();
        properties.setProperty( "artifactId", artifactId );
        properties.setProperty( "groupId", groupId );
        properties.setProperty( "version", version );
        properties.setProperty( "module-name-no-spaces", moduleNameNoSpaces );
        properties.setProperty( "module-name", moduleName );
        properties.setProperty( "module-description", moduleDescription );
        properties.setProperty( "module-author", moduleAuthor );
        properties.setProperty( "openmrs-version", openmrsVersion);
        properties.setProperty( "service-dao-name-no-spaces", serviceDaoName);
        properties.setProperty( "admin-page-link", adminPageLink );
        properties.setProperty( "object-name-no-spaces", objectName);
        properties.setProperty( "package", packageName);
        properties.setProperty( "adminLinkReply", adminLinkReply);
        properties.setProperty( "springMvcReply",springMvcReply);
        properties.setProperty( "serviceReply", serviceReply);
        properties.setProperty("dependentModules",dependentModules);
        properties.setProperty( "dependencyManagement", Pattern.matches("^1\\.[5-7].+",openmrsVersion)?"y":"n");
        session.getExecutionProperties().putAll( properties );
        
        
                       
        // Using custom prompts, avoid archetype plugin interaction
        setPrivateField( "interactiveMode", Boolean.FALSE );
        setPrivateField("archetypeArtifactId", archetypeArtifactId);
        setPrivateField( "archetypeGroupId", archetypeGroupId );
        setPrivateField( "archetypeVersion", archetypeVersion );
        setPrivateField( "archetypeRepository", archetypeRepository );
        setPrivateField( "archetypeCatalog", archetypeCatalog );
        setPrivateField( "localRepository", localRepository );
        setPrivateField( "remoteArtifactRepositories", remoteArtifactRepositories );
        setPrivateField( "basedir", basedir );
        setPrivateField( "session", session );
        setPrivateField( "goals", goals );
        setPrivateField( "archetype", archetype );
        setPrivateField( "selector", selector );
        setPrivateField( "configurator", configurator );
        setPrivateField( "generator", generator );
        setPrivateField( "invoker", invoker );
        
        List<String> archetypeIds = new ArrayList<String>();
        
            
            archetypeIds.add( "openmrs-archetype-basicmodule-creation" );

            if( "y".equalsIgnoreCase(adminLinkReply)) {
                archetypeIds.add( "openmrs-archetype-adminpagelink-creation" );
                
            }  
            if ( "y".equalsIgnoreCase(springMvcReply) ) { 
                archetypeIds.add( "openmrs-archetype-springmvc-creation" );
                
            } 
            if ( "y".equalsIgnoreCase(serviceReply) ) { 
                archetypeIds.add( "openmrs-archetype-service-dao-hibernate-creation" );
            }
                    
        
        
        for( String archetypeId : archetypeIds ) {
            getLog().info("Archetype: " + archetypeId);
            
            setPrivateField( "archetypeArtifactId", archetypeId );
            
            // Execute creating archetype for each archetype id
            super.execute();
        }       
        
    }
    
    /**
     * Removes spaces from the WizardMojo fields.
     * Mainly used for setting fields whose values are used for naming files in archetypes. 
     */
    protected void setParameterWithOutSpaces(String fieldName , String value) throws NoSuchFieldException, IllegalAccessException{
  
    	Field fi=WizardMojo.class.getDeclaredField(fieldName);
    	String brk[]=value.split(" ");
    	String valueWithNoSpace="";
    	for (String string : brk) {
    	    valueWithNoSpace+=string;
    	}    	
    	fi.set(this, valueWithNoSpace);
    	
    }

 
    protected void setPrivateField(String fieldName, Object value) throws MojoExecutionException {
        try {
            Class<?> superClass = this.getClass().getSuperclass();
            Field field = superClass.getDeclaredField( fieldName );
            field.setAccessible( true ); // Allow access to private field
            field.set( this, value );
        } catch (Exception e) {
            throw new MojoExecutionException("Unable to set mojo field: " + fieldName, e);
        }
    }
}