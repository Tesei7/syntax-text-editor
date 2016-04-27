package ru.tesei7.textEditor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoadedFileTest {
	@InjectMocks
	@Spy
	private LoadedFile loadedFile;

	@Test
	public void testGetExtension() throws Exception {
		doReturn(null).when(loadedFile).getFileName();
		assertThat(loadedFile.getExtension(), is(""));
		doReturn("123").when(loadedFile).getFileName();
		assertThat(loadedFile.getExtension(), is(""));
		doReturn("123.txt").when(loadedFile).getFileName();
		assertThat(loadedFile.getExtension(), is("txt"));
		doReturn("123.JAvA").when(loadedFile).getFileName();
		assertThat(loadedFile.getExtension(), is("java"));
		doReturn("123.JAvA.js").when(loadedFile).getFileName();
		assertThat(loadedFile.getExtension(), is("js"));
	}

	@Test
	public void testGetFileName() throws Exception {
		loadedFile = new LoadedFile(null, null);
		assertThat(loadedFile.getFileName(), is(""));
		
		File file = mock(File.class);
		when(file.getName()).thenReturn("123");
		loadedFile = new LoadedFile(null, file);
		assertThat(loadedFile.getFileName(), is("123"));
	}

}
