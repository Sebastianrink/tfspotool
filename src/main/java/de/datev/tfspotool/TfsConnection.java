package de.datev.tfspotool;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.framework.configuration.entities.ProjectCollectionEntity;
import com.microsoft.tfs.core.clients.framework.configuration.entities.TeamProjectEntity;
import com.microsoft.tfs.core.clients.workitem.CoreFieldReferenceNames;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.clients.workitem.WorkItemClient;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.microsoft.tfs.core.clients.workitem.query.WorkItemCollection;
import com.microsoft.tfs.core.clients.workitem.wittype.WorkItemType;
import com.microsoft.tfs.core.httpclient.DefaultNTCredentials;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TfsConnection {

    public static final String TYPE_EPIC = "Epic";
    public static final String TYPE_INITIATIVE = "Initiative";
    public static final String TYPE_GOAL = "Goal";
    public static final String goalDescription = "<p><strong>Kurzbeschreibung:</strong> </p><p>Beschreiben Sie die Idee, das Problem, den Lösungsansatz und den Nutzen (für den Kunden/für die DATEV)\n" +
            "</p><p><br></p><p><strong>Risiko/Folgen bei Nichtdurchführung/Nichtverfolgung</strong></p><p>\n" +
            "    </p><p><strong>Dringlichkeit</strong>: Insbs. bei MUST-Goals: Warum ist das Thema ein MUST? Bis zu welchem Zeitpunkt muss das Thema abgeschlossen sein und warum?\n" +
            "</p><p><br></p><p><strong>Betrifft</strong> (Aufzählung von Abhängigkeiten UND welche Anwendungen, Komponenten, Bereiche, Teams etc. sind betroffen/werden benötigt)</p>\n";
    public static final String initiativeDescription =
            "<p><strong>Kurzbeschreibung:</strong> </p><p>Welchen Mehrwert erhält der Kunde im Rahmen der Initiative/Was ist der Inhalt der Initiative?\n" +
            "</p><p><br></p><p><strong>Dringlichkeit:</strong></p><p> Insbs. bei MUST-Initiativen: Warum ist die Initiative ein MUST? Bis zu welchem Zeitpunkt muss das Thema abgeschlossen sein und warum?\n" +
            "</p><p><br></p><p><strong>Betrifft:</strong></p><p> Aufzählung von Abhängigkeiten UND welche Anwendungen, Komponenten, Bereiche, Teams etc. sind betroffen/werden benötigt)";


    public static final String goalDefinitionOfDone = "Akzeptanzkriterien:";

    private static final String GET_WORK_ITEMS_BY_AWESOME_TYPE = "Select ID, Title from WorkItems where (System.WorkItemType = '%s')";
    private final TFSTeamProjectCollection tfsTeamProjectCollection;
    private final WorkItemClient workItemClient;
    private final Project project;


    public TfsConnection() {
        System.setProperty("com.microsoft.tfs.jni.native.base-directory", "src\\main\\resources\\tfs\\nativelibs");
        URI uri;
        try {
            //uri = new URI("http://tfs-e.bk.datev.de:8080/tfs/C3");
            uri = new URI("http://vmnue540:8080/tfs/EQ21TestCollection/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        tfsTeamProjectCollection = new TFSTeamProjectCollection(uri, new DefaultNTCredentials());
        workItemClient = tfsTeamProjectCollection.getWorkItemClient();
        project = workItemClient.getProjects().get("Test107");
    }

    public Date setStartDate(int goalToChangeID, Date startDate) throws URISyntaxException {
        WorkItem  actual = getWorkItemById(goalToChangeID);
        actual.getFields().getField("Start Date").setValue(startDate);
        actual.save();
        return (Date) actual.getFields().getField("Start Date").getValue();
    }


    public WorkItem getWorkItemById(int id) throws URISyntaxException {
        return workItemClient.getWorkItemByID(id);
    }

    public TeamProjectEntity[] getProjects() {
        ProjectCollectionEntity teamProjectCollectionEntity = tfsTeamProjectCollection.getTeamProjectCollectionEntity(true);

        return teamProjectCollectionEntity.getTeamProjects();
    }

    public List<WorkItem> getAllEpics() {
        return getWorkItemsByType(TYPE_EPIC);
    }

    public List<WorkItem> getAllInitiatives() {
        return getWorkItemsByType(TYPE_INITIATIVE);
    }

    public List<WorkItem> getAllGoals() {
        return getWorkItemsByType(TYPE_GOAL);
    }

    private List<WorkItem> getWorkItemsByType(final String type) {
        final WorkItemCollection workItems = workItemClient.query(String.format(GET_WORK_ITEMS_BY_AWESOME_TYPE, type));
        final List<WorkItem> workItemList = new ArrayList<>();
        for (int i = 0; i < workItems.size(); i++) {
            workItemList.add(workItems.getWorkItem(i));
        }
        return workItemList;
    }


    public Date setEndDate(int goalToChangeID, Date endDate) throws URISyntaxException {
        WorkItem  actual = getWorkItemById(goalToChangeID);
        actual.getFields().getField("Finish Date").setValue(endDate);
        actual.save();
        return (Date) actual.getFields().getField("Finish Date").getValue();
    }

    public int createInitiative(String title, String koe, String defaultArea, String iteration, int priority, double stackRank, String acceptanceCriteria) throws Exception {
        // Find the work item type matching the specified name.
        final WorkItemType initiativeWorkItemType = project.getWorkItemTypes().get("Initiative"); //$NON-NLS-1$

        // Create a new work item of the specified type.
        final WorkItem newWorkItem = project.getWorkItemClient().newWorkItem(initiativeWorkItemType);

        // Set the title on the work item.
        newWorkItem.setTitle("I - " + koe + " " + title);
        newWorkItem.getFields().getField(CoreFieldReferenceNames.AREA_PATH).setValue(defaultArea);
        newWorkItem.getFields().getField(CoreFieldReferenceNames.ITERATION_PATH).setValue(iteration);
        newWorkItem.getFields().getField("Priority").setValue(priority);
        newWorkItem.getFields().getField("Stack Rank").setValue(stackRank);
        newWorkItem.getFields().getField(CoreFieldReferenceNames.DESCRIPTION).setValue(initiativeDescription);
        newWorkItem.getFields().getField("DefinitionofDone").setValue(acceptanceCriteria);

        newWorkItem.save();
        return newWorkItem.getID();
    }

    public int createGoal(Goal goal) {
        // Find the work item type matching the specified name.
        final WorkItemType goalWorkItemType = project.getWorkItemTypes().get("Goal"); //$NON-NLS-1$

        // Create a new work item of the specified type.
        final WorkItem newWorkItem = project.getWorkItemClient().newWorkItem(goalWorkItemType);

        // Set the title on the work item.
        newWorkItem.setTitle("G - " + goal.getKoe() + " " + goal.getTitle());

        // Add a comment as part of the change
        newWorkItem.getFields().getField(CoreFieldReferenceNames.AREA_PATH).setValue(goal.getDefaultArea());
        newWorkItem.getFields().getField(CoreFieldReferenceNames.ITERATION_PATH).setValue(goal.getIteration());
        newWorkItem.getFields().getField("Priority").setValue(goal.getPriority());
        newWorkItem.getFields().getField("DefinitionofDone").setValue(goal.getDetails());
        newWorkItem.getFields().getField(CoreFieldReferenceNames.DESCRIPTION).setValue(goal.getGoalDescription());

        // Save the new work item to the server.
        newWorkItem.save();

        return newWorkItem.getID();
    }

    public int createInitiative(Initiative initiative) {
        final WorkItemType initiativeWorkItemType = project.getWorkItemTypes().get("Initiative"); //$NON-NLS-1$

        // Create a new work item of the specified type.
        final WorkItem newWorkItem = project.getWorkItemClient().newWorkItem(initiativeWorkItemType);

        // Set the title on the work item.
        newWorkItem.setTitle("I - " + initiative.getKoe() + " " + initiative.getTitle());
        newWorkItem.getFields().getField(CoreFieldReferenceNames.AREA_PATH).setValue(initiative.getDefaultArea());
        newWorkItem.getFields().getField(CoreFieldReferenceNames.ITERATION_PATH).setValue(initiative.getIteration());
        newWorkItem.getFields().getField("Priority").setValue(initiative.getPriority());
        newWorkItem.getFields().getField("Stack Rank").setValue(initiative.getStackRank());
        newWorkItem.getFields().getField(CoreFieldReferenceNames.DESCRIPTION).setValue(initiativeDescription);
        newWorkItem.getFields().getField("DefinitionofDone").setValue(initiative.getAcceptanceCriteria());

        newWorkItem.save();
        return newWorkItem.getID();
    }
}
