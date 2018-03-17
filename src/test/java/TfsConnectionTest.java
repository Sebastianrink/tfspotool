import com.microsoft.tfs.core.clients.framework.configuration.entities.TeamProjectEntity;
import com.microsoft.tfs.core.clients.workitem.CoreFieldReferenceNames;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import de.datev.tfspotool.Goal;
import de.datev.tfspotool.GoalPriority;
import de.datev.tfspotool.Initiative;
import de.datev.tfspotool.TfsConnection;
import org.junit.Assert;
import org.junit.Test;

import java.net.URISyntaxException;
import java.time.*;
import java.util.Date;
import java.util.List;


public class TfsConnectionTest {

    private final TfsConnection tfsConnection = new TfsConnection();
    private String defaultArea = "Test107\\OnPremise\\ADMIN\\DASO";
    private  String koe = "EM21";
    private  String title = "my fancy title";
    private  String iteration = "Test107\\10.0";
    private  int priority = 1;
    private  String goalDescription = TfsConnection.goalDescription;
    private  String details = TfsConnection.goalDefinitionOfDone;




    @Test
    public void getProjectName_Test107() throws URISyntaxException {
        //arrange
        TeamProjectEntity[] projects = tfsConnection.getProjects();
        String expected = "Test107";
        //act
        String actual = projects[0].getProjectName();
        //assert
        Assert.assertEquals(actual,expected);
    }

    @Test
    public void getWorkItemById_correctTitle() throws URISyntaxException {
        //arrange
        WorkItem workItem = tfsConnection.getWorkItemById(81293);
        String expectedTitle = "CR DASO silent Installation";
        //act
        String title = workItem.getTitle();
        //assert
        Assert.assertEquals(expectedTitle, title);
    }

    @Test
    public void getAllEpics_epicsReturned() throws Exception {
        //arrange

        //act
        List<WorkItem> allEpics = tfsConnection.getAllEpics();
        //assert
        Assert.assertEquals(TfsConnection.TYPE_EPIC, allEpics.get(0).getType().getName());
    }

    @Test
    public void getAllInitiatives_initiativesReturned() throws Exception {
        //arrange

        //act
        List<WorkItem> allInitiatives = tfsConnection.getAllInitiatives();
        //assert
        Assert.assertEquals(TfsConnection.TYPE_INITIATIVE, allInitiatives.get(0).getType().getName());
    }

    @Test
    public void getAllGoals_goalsReturned() throws Exception {
        //arrange

        //act
        List<WorkItem> allGoals = tfsConnection.getAllGoals();

        //assert
        Assert.assertEquals(TfsConnection.TYPE_GOAL, allGoals.get(0).getType().getName());
    }


    // Leading Organizational Unit: führende Organisationseinheit (eine!)



    @Test
    public void addNewGoalNew_goalCreated() throws Exception {
        //arrange
        Goal goal = new Goal();
        goal.setKoe(koe);
        goal.setDefaultArea(defaultArea);
        goal.setTitle(title);
        goal.setIteration(iteration);
        goal.setPriority(priority);
        goal.setGoalDescription(goalDescription);
        goal.setDetails(details);

        String expectedTitle = "G - " + koe + " " + title;

        //act
        final int goalId = tfsConnection.createGoal(goal);

        //assert
        final WorkItem createdGoal = tfsConnection.getWorkItemById(goalId);
        Assert.assertEquals(expectedTitle, createdGoal.getTitle());
        Assert.assertEquals(defaultArea, createdGoal.getFields().getField(CoreFieldReferenceNames.AREA_PATH).getValue());
        Assert.assertEquals(iteration, createdGoal.getFields().getField(CoreFieldReferenceNames.ITERATION_PATH).getValue());
        Assert.assertEquals(GoalPriority.MUST.ordinal(), createdGoal.getFields().getField("Priority").getValue());
        Assert.assertTrue(createdGoal.getFields().getField(CoreFieldReferenceNames.DESCRIPTION).getValue().toString().contains(goalDescription));
        Assert.assertEquals(details, createdGoal.getFields().getField("DefinitionofDone").getValue());
    }

    @Test
    public void setStartDate_01012017_01012017() throws URISyntaxException {
        //Arrange
        int goalToChangeID =111421;

        LocalDate localDate = LocalDate.of(2017, 1, 1);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.now());
        Date startDate = Date.from(localDateTime.toInstant(ZoneOffset.UTC));

        //Act
        Date actual = tfsConnection.setStartDate (goalToChangeID, startDate);

        //Assert
        Assert.assertEquals(startDate, actual);
    }

    @Test
    public void setEndDate_02012017_02012017() throws URISyntaxException {
        //Arrange
        int goalToChangeID =111421;

        LocalDate localDate = LocalDate.of(2017, 1, 2);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.now());
        Date endDate = Date.from(localDateTime.toInstant(ZoneOffset.UTC));

        //Act
        Date actual = tfsConnection.setEndDate (goalToChangeID, endDate);

        //Assert
        Assert.assertEquals(endDate, actual);
    }

    @Test
    public void addNewInitiative_initiativeCreated() throws Exception {
        //arrange
        double stackRank = 1;
        String acceptanceCriteria = "Die Initiative ist abgeschlossen wenn...";
        String expectedTitle = "I - "+ koe+" " + title;

        Initiative initiative = new Initiative();
        initiative.setKoe(koe);
        initiative.setTitle(title);
        initiative.setIteration(iteration);
        initiative.setPriority(priority);
        initiative.setStackRank(stackRank);
        initiative.setDefaultArea(defaultArea);
        initiative.setAcceptanceCriteria(acceptanceCriteria);
        final int initiativeId = tfsConnection.createInitiative(initiative);


        //assert
        final WorkItem newinitiative = tfsConnection.getWorkItemById(initiativeId);
        Assert.assertEquals(expectedTitle, newinitiative.getTitle());
        Assert.assertEquals(iteration, newinitiative.getFields().getField(CoreFieldReferenceNames.ITERATION_PATH).getValue());
        Assert.assertEquals(defaultArea, newinitiative.getFields().getField(CoreFieldReferenceNames.AREA_PATH).getValue());
        Assert.assertEquals(TfsConnection.initiativeDescription, newinitiative.getFields().getField(CoreFieldReferenceNames.DESCRIPTION).getValue());
        Assert.assertEquals(GoalPriority.MUST.ordinal(), newinitiative.getFields().getField("Priority").getValue());
        Assert.assertEquals(stackRank, newinitiative.getFields().getField("Stack Rank").getValue());
        Assert.assertEquals(acceptanceCriteria, newinitiative.getFields().getField("DefinitionofDone").getValue());
    }



}
