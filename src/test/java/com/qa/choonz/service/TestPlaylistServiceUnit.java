package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.domain.User;
import com.qa.choonz.persistence.repository.PlaylistRepository;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.PlaylistDTO;

@SpringBootTest
class TestPlaylistServiceUnit {

	@Autowired
	private PlaylistService service;
	
	@MockBean
	private PlaylistRepository repo;
	
	@MockBean
    private TrackRepository trackRepo;
	
	@MockBean
	private ModelMapper modelMapper;
	
    private PlaylistDTO mapToDTO(Playlist playlist) {
        return this.modelMapper.map(playlist, PlaylistDTO.class);
    }
    
    private List<Playlist> playlistList;
    private Playlist testPlaylist;
    private Playlist testPlaylistWithId;
    private PlaylistDTO playlistDTO;
    private User testUser;
    
    final Long id = 1L;
    final String name = "Tunes";
    final String description = "Bangers only";
    final String artwork = "artwork";
    List<Track> tracks;
    
    @BeforeEach
    void init() {
    	this.playlistList = new ArrayList<>();
    	this.tracks = new ArrayList<>();
    	this.testPlaylist = new Playlist(this.id,this.name,this.description
    			,this.artwork,this.tracks, this.testUser);
    	this.playlistList.add(testPlaylist);
    	this.playlistDTO = this.mapToDTO(testPlaylist);
    }
    
    @Test
    void createTest() {
    	when(this.repo.save(this.testPlaylist)).thenReturn(this.testPlaylistWithId);
    	
    	when(this.modelMapper.map(this.testPlaylistWithId,PlaylistDTO.class)).thenReturn(this.playlistDTO);
    	
    	PlaylistDTO expected = this.playlistDTO;
    	PlaylistDTO actual = this.service.create(this.testPlaylist);
    	assertThat(expected).isEqualTo(actual);
    	
    	verify(this.repo, times(1)).save(this.testPlaylist);
    }
    
    @Test
    void readOneTest() {
		when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testPlaylist));
		
		when(this.modelMapper.map(testPlaylistWithId, PlaylistDTO.class)).thenReturn(playlistDTO);
		
		assertThat(this.playlistDTO).isEqualTo(this.service.read(this.id));
		
		verify(this.repo, times(1)).findById(this.id);
    }
    
    @Test
    void realAllTest() {
        when(repo.findAll()).thenReturn(this.playlistList);

        when(this.modelMapper.map(testPlaylistWithId, PlaylistDTO.class)).thenReturn(playlistDTO);

        assertThat(this.service.read().isEmpty()).isFalse();

        verify(repo, times(1)).findAll();
    }
    
    @Test
    void updateTest() {
    	Playlist playlist = new Playlist(this.id,this.name,this.description,this.artwork,this.tracks, this.testUser);
    	
    	PlaylistDTO playlistDTO = new PlaylistDTO(this.id,this.name,this.description,this.artwork,this.tracks, this.testUser);
    	
    	Playlist updatedPlaylist = new Playlist(this.id,
    			playlistDTO.getName(),
    			playlistDTO.getDescription(),
    			playlistDTO.getArtwork(),
    			this.tracks, this.testUser);
    	
    	PlaylistDTO updatedPlaylistDTO = new PlaylistDTO(this.id,updatedPlaylist.getName()
    			,updatedPlaylist.getDescription()
    			,updatedPlaylist.getArtwork()
    			,updatedPlaylist.getTracks()
    			,updatedPlaylist.getUser());
    	
		when(this.repo.findById(this.id)).thenReturn(Optional.of(playlist));
		
		when(this.repo.save(playlist)).thenReturn(updatedPlaylist);
		
		when(this.modelMapper.map(updatedPlaylist, PlaylistDTO.class)).thenReturn(updatedPlaylistDTO);
		
		assertThat(updatedPlaylistDTO).isEqualTo(this.service.update(testPlaylist, this.id));
		
		verify(this.repo, times(1)).findById(1L);
		verify(this.repo, times(1)).save(updatedPlaylist);
    }
    
	@Test
	void testDelete() {
		when(this.repo.existsById(id)).thenReturn(true, false);
		
		assertThat(this.service.delete(id)).isFalse();
		verify(this.repo, times(1)).deleteById(id);
		verify(this.repo, times(1)).existsById(id);
	}
	
//    @Test
//    void addTrackTest() {
	
//    }
    
//    @Test
//    void removeTrackTest() {

//    }
	
}
