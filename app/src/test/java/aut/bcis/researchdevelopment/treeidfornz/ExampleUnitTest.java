package aut.bcis.researchdevelopment.treeidfornz;

import android.app.Activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;

import aut.bcis.researchdevelopment.adapter.TreeAdapter;
import aut.bcis.researchdevelopment.model.ListHeader;
import aut.bcis.researchdevelopment.model.Tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Mock
    Activity mMockContext;
    TreeAdapter treeAdapter;
    @Test
    public void testTreeAdapterObjectCount() {
        Tree testTree = new Tree();
        ListHeader treeHeader = new ListHeader();
        ArrayList treeList = new ArrayList<>();
        treeList.add(testTree);
        treeList.add(treeHeader);
        treeAdapter = new TreeAdapter(mMockContext, treeList);
        assertEquals(2, treeAdapter.getViewTypeCount());
    }
    @Test
    public void testTreeApdaterSize() {
        Tree testTree1 = new Tree();
        Tree testTree2 = new Tree();
        Tree testTree3 = new Tree();
        ArrayList treeList = new ArrayList<>();
        treeList.add(testTree1);
        treeList.add(testTree2);
        treeList.add(testTree3);
        treeAdapter = new TreeAdapter(mMockContext, treeList);
        assertEquals(3, treeAdapter.getCount());
    }
    @Test
    public void testTreeAdapterItemFilter() {
        ArrayList treeList = new ArrayList<>();
        Tree testTree1 = new Tree();
        Tree testTree2 = new Tree();
        treeList.add(testTree1);
        treeList.add(testTree2);
        treeAdapter = new TreeAdapter(mMockContext, treeList);
        assertNotNull("Item filter is not null", treeAdapter.getFilter());
    }
    @Test
    public void testDeleteFavouriteObject() {
        ArrayList treeList = new ArrayList<>();
        Tree testTree1 = new Tree();
        Tree testTree2 = new Tree();
        treeList.add(testTree1);
        treeList.add(testTree2);
        treeAdapter = new TreeAdapter(mMockContext, treeList);
        treeList.remove(testTree1);
        assertEquals(1, treeAdapter.getCount());
    }
    @Test
    public void testAdapterItemViewType() {
        Tree testTree = new Tree();
        ListHeader treeHeader = new ListHeader();
        ArrayList treeList = new ArrayList<>();
        treeList.add(treeHeader);
        treeList.add(testTree);
        treeAdapter = new TreeAdapter(mMockContext, treeList);
        assertEquals(0, treeAdapter.getItemViewType(0));
        assertEquals(1, treeAdapter.getItemViewType(1));
    }
    @Test
    public void testAdapterGetItem() {
        Tree testTree = new Tree();
        ListHeader treeHeader = new ListHeader();
        ArrayList treeList = new ArrayList<>();
        treeList.add(treeHeader);
        treeList.add(testTree);
        treeAdapter = new TreeAdapter(mMockContext, treeList);
        assertEquals(treeHeader, treeAdapter.getItem(0));
        assertEquals(testTree, treeAdapter.getItem(1));
    }
    @Test
    public void testGenerateAlphabeticalHeadersAlgorithm() {
        ArrayList<Object> treeList = new ArrayList<>();
        Tree tree1 = new Tree();
        tree1.setCommonName("Akeake");
        Tree tree2 = new Tree();
        tree2.setCommonName("Hinau");
        Collections.addAll(treeList, tree1, tree2);
        Utility.generateAlphabeticalHeaders(treeList);
        ListHeader lh1 = (ListHeader) treeList.get(0);
        ListHeader lh2 = (ListHeader) treeList.get(2);
        assertEquals("A", lh1.getName());
        assertEquals("H", lh2.getName());
    }
    @Test
    public void testGenerateGenusHeadersAlgorithm() {
        ArrayList<Object> treeList = new ArrayList<>();
        Tree tree1 = new Tree();
        tree1.setGenus("ExampleGenus1");
        Tree tree2 = new Tree();
        tree2.setGenus("ExampleGenus2");
        Collections.addAll(treeList, tree1, tree2);
        Utility.generateGenusHeaders(treeList);
        ListHeader lh1 = (ListHeader) treeList.get(0);
        ListHeader lh2 = (ListHeader) treeList.get(2);
        assertEquals("ExampleGenus1", lh1.getName());
        assertEquals("ExampleGenus2", lh2.getName());
    }
    @Test
    public void testGenerateFamilyHeadersAlgorithm() {
        ArrayList<Object> treeList = new ArrayList<>();
        Tree tree1 = new Tree();
        tree1.setFamily("ExampleFamily1");
        Tree tree2 = new Tree();
        tree2.setFamily("ExampleFamily2");
        Collections.addAll(treeList, tree1, tree2);
        Utility.generateFamilyHeaders(treeList);
        ListHeader lh1 = (ListHeader) treeList.get(0);
        ListHeader lh2 = (ListHeader) treeList.get(2);
        assertEquals("ExampleFamily1", lh1.getName());
        assertEquals("ExampleFamily2", lh2.getName());
    }
    @Test
    public void testTestFormatDynamicQuery() {
        String query = "SELECT * FROM TREE WHERE AND MARGIN = 'toothed' AND ARRANGEMENT = 'opposite'";
        String formattedQuery = query.replaceFirst(query.substring(24,28),"");
        assertEquals("SELECT * FROM TREE WHERE MARGIN = 'toothed' AND ARRANGEMENT = 'opposite'", formattedQuery);
    }
    @Test
    public void testSortTypeCheckCommonNameCase() {
        ArrayList<Object> treeList = new ArrayList<>();
        Tree tree1 = new Tree();
        tree1.setCommonName("Akeake");
        Tree tree2 = new Tree();
        tree2.setCommonName("Hinau");
        Collections.addAll(treeList, tree1, tree2);
        Utility.sortTypeSwitch("CommonName", treeList);
        ListHeader lh1 = (ListHeader) treeList.get(0);
        ListHeader lh2 = (ListHeader) treeList.get(2);
        assertEquals("A", lh1.getName());
        assertEquals("H", lh2.getName());
    }
    @Test
    public void testSortTypeCheckGenusCase() {
        ArrayList<Object> treeList = new ArrayList<>();
        Tree tree1 = new Tree();
        tree1.setGenus("ExampleGenus1");
        Tree tree2 = new Tree();
        tree2.setGenus("ExampleGenus2");
        Collections.addAll(treeList, tree1, tree2);
        Utility.sortTypeSwitch("Genus", treeList);
        ListHeader lh1 = (ListHeader) treeList.get(0);
        ListHeader lh2 = (ListHeader) treeList.get(2);
        assertEquals("ExampleGenus1", lh1.getName());
        assertEquals("ExampleGenus2", lh2.getName());
    }
    @Test
    public void testSortTypeCheckFamilysCase() {
        ArrayList<Object> treeList = new ArrayList<>();
        Tree tree1 = new Tree();
        tree1.setFamily("ExampleFamily1");
        Tree tree2 = new Tree();
        tree2.setFamily("ExampleFamily2");
        Collections.addAll(treeList, tree1, tree2);
        Utility.sortTypeSwitch("Family", treeList);
        ListHeader lh1 = (ListHeader) treeList.get(0);
        ListHeader lh2 = (ListHeader) treeList.get(2);
        assertEquals("ExampleFamily1", lh1.getName());
        assertEquals("ExampleFamily2", lh2.getName());
    }
    @Test
    public void testAnalyzedReceivedDynamicQuery() {
        String exampleQuery = "SELECT * FROM TREE WHERE MARGIN = 'toothed' AND ARRANGEMENT = 'opposite'";
        ArrayList<String> chosenKeys = new ArrayList<String>();
        if(exampleQuery.contains("toothed")) {
            chosenKeys.add("toothed");
        }
        if(exampleQuery.contains("smooth")) {
            chosenKeys.add("smooth");
        }
        if(exampleQuery.contains("hand-shaped")) {
            chosenKeys.add("hand-shaped");
        }
        if(exampleQuery.contains("alternating")) {
            chosenKeys.add("alternating");
        }
        if(exampleQuery.contains("opposite")) {
            chosenKeys.add("opposite");
        }
        assertEquals(2, chosenKeys.size());
        assertEquals("toothed", chosenKeys.get(0));
        assertEquals("opposite", chosenKeys.get(1));
    }
}