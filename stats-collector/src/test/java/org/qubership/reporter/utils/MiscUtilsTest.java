package org.qubership.reporter.utils;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MiscUtilsTest {
    @Test
    public void testGetChunkFromList_NormalCase() {
        List<String> sourceList = List.of("a", "b", "c", "d", "e", "f", "g");
        List<String> actList = MiscUtils.getChunk(sourceList, 5);
        List<String> expList = List.of("a", "b", "c", "d", "e");
        assertEquals(expList, actList);
    }

    @Test
    public void testGetChunkFromList_SourceIsLessThanRequested() {
        List<String> sourceList = List.of("a", "b", "c");
        List<String> actList = MiscUtils.getChunk(sourceList, 15);
        List<String> expList = List.of("a", "b", "c");
        assertEquals(expList, actList);
    }

    @Test
    public void testGetChunkFromList_SourceIsEmpty() {
        List<String> sourceList = List.of();
        List<String> actList = MiscUtils.getChunk(sourceList, 15);
        List<String> expList = List.of();
        assertEquals(expList, actList);
    }

    @Test
    public void testGetChunkFromList_SourceIsLessThanRequested2() {
        List<String> sourceList = List.of("a", "b", "c", "d", "e", "f", "g");
        List<String> actList = MiscUtils.getChunk(sourceList, 150);
        List<String> expList = List.of("a", "b", "c", "d", "e", "f", "g");
        assertEquals(expList, actList);
    }
}
