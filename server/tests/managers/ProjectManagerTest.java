package managers;

import constants.ProjectConflicts;
import models.project.Diff;
import org.junit.Before;
import org.junit.Test;
import utils.ConflictResolver;
import utils.ServerTest;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class ProjectManagerTest extends ServerTest {
    ProjectManager projectManager;
    File localFile;
    File conflictFile;

    @Before
    public void setUp() throws Exception {
        localFile = new File("tests/utils/diffsFiles/local.diff");
        conflictFile = new File("tests/utils/diffsFiles/oneConflictOneSimpleAdd.diff");
        projectManager = new ProjectManager();
    }

    @Test
    public void testCreateTikzFromDiffs() throws Exception {
        ConflictResolver conflictResolver = projectManager.getConflictResolver();
        conflictResolver.update(localFile,conflictFile);
        List<Diff> resolvedDiff = conflictResolver.resolve(ProjectConflicts.SAVE_USER_VERSION_ONLY);
        String tikzGraph = projectManager.createTikzFromDiffs(resolvedDiff);
        assertEquals("\\node[circle, draw, radius=1.25](02165a2d-55da-4db7-8ab0-7fc2d3807e53) at (-5.794,7.091){};\n" +
               "\\node[rectangle, draw](974bde44-205b-442d-ae20-bea5ce297951) at (4.657,3.307){};\n", tikzGraph);

        conflictResolver.update(localFile,conflictFile);
        List<Diff> resolvedDiff2 = conflictResolver.resolve(ProjectConflicts.SAVE_USER_VERSION);
        String tikzGraph2 = projectManager.createTikzFromDiffs(resolvedDiff2);
        assertEquals("\\node[circle, draw, radius=1.25](02165a2d-55da-4db7-8ab0-7fc2d3807e53) at (-5.794,7.091){};\n" +
                "\\node[rectangle, draw](974bde44-205b-442d-ae20-bea5ce297951) at (4.657,3.307){};\n" +
                "\\node[circle, draw, radius=1.25](7900b1f6-7420-4000-bc05-e88660d72e3c) at (8.440,2.884){};\n", tikzGraph2);
    }
}