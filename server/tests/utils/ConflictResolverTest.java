package utils;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import constants.ProjectConflicts;

public class ConflictResolverTest {
    ConflictResolver conflictResolver = new ConflictResolver();
    File baseFile;
    File localFile;
    File conflictFile;
    File oneConflictOneSimpleAdd;

    @Before
    public void setUp() throws Exception {
        baseFile = new File("tests/utils/diffsFiles/base.diff");
        localFile = new File("tests/utils/diffsFiles/local.diff");
        conflictFile = new File("tests/utils/diffsFiles/conflict.diff");
        oneConflictOneSimpleAdd = new File("tests/utils/diffsFiles/oneConflictOneSimpleAdd.diff");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testcheckHasConflict() throws Exception {
        conflictResolver.update(localFile, baseFile);
        assertEquals(conflictResolver.checkDiagramHasConflict(), ProjectConflicts.MERGE_OK);

        conflictResolver.update(localFile, conflictFile);
        assertEquals(conflictResolver.checkDiagramHasConflict(), ProjectConflicts.NO_LOCAL_MODIFICATION);

        conflictResolver.update(localFile, oneConflictOneSimpleAdd);
        assertEquals(conflictResolver.checkDiagramHasConflict(), ProjectConflicts.MERGE_HAS_CONFLICTS);
    }
}
