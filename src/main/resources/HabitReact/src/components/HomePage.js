/* eslint-disable */
import React, { Component } from 'react';
import {
  Map,
  GoogleApiWrapper,
  InfoWindow,
  Marker
} from 'google-maps-react';
import uuidv4 from 'uuid/v4';
import './HomePage.scss';
import Placeholder from './placeholder.svg';
import Add from './Add.svg';
import Stop from './Stop.svg';
import Play from './Play.svg';
import ProfileDisabled from './Profile-disabled.svg';
import DiscoveryDisabled from './Discovery-disabled.svg';
import MapsDisabled from './Maps-disabled.svg';
import FiltersSearch from './Filters.svg';
import Constants from './constants';
// let API = "http://c02x19lwjg5j:8080";
// API = "http://ec2-52-10-235-171.us-west-2.compute.amazonaws.com";
// /admin/user

let API = Constants.API;

const mapStyles = {
  width: '375px',
  height: '667px'
};

const coords = { lat: -21.805149, lng: -49.0921657 };
// console.log('coords are', coords);

// console.log('sttart recognition');
// recognition.start();

export class HomePage extends Component {
  constructor(props) {
    super(props);
    if (this.props.location && !this.props.location.state) {
      this.props.history.push('/login');
    }

    this.audioBlob = null;
    this.state = {
      searchTag: '',
      user: 'sachin1',
      userId: props.location.state.userId,
      showingInfoWindow: false,
      activeMarker: {},
      selectedPlace: {},
      showMarkerDetails: null,
      newMarkerInProgress: null,
      newMarkerInProgressId: null,
      markers: [
        
      ],
      userList: null,
      showTimer: false,
      audioPaused: false
    };
  }

  getUserIdByUsername = (loggedUsername) => {
    if (this.state.userList) {
      let userid = null;
      Object.keys(this.state.userList).forEach((key) => {
        const user = this.state.userList[key];
        if (user.username === loggedUsername) {
          userid = user.userId
        }
      });
      return userid;
    }
    return null;
  };

  setupSpeechRecognition = () => {
    try {
      var SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
      this.recognition = new SpeechRecognition();
    } catch (err) {
      console.log('speech recognition error', err);
    }
    this.recognition.continuous = true;
    this.recognition.onresult = (event) => {
      // event is a SpeechRecognitionEvent object.
      // It holds all the lines we have captured so far. 
      // We only need the current one.
      var current = event.resultIndex;
      // Get a transcript of what was said.
      var transcript = event.results[current][0].transcript;

      // Add the current transcript to the contents of our Note.
      // There is a weird bug on mobile, where everything is repeated twice.
      // There is no official solution so far so we have to handle an edge case.
      var mobileRepeatBug = (current == 1 && transcript == event.results[0][0].transcript);
      const { newMarkerInProgress, markers, newMarkerInProgressId } = this.state;
      let markerToUpload = null;
      markers.forEach((marker) => {
        if (marker.id === newMarkerInProgressId) {
          marker.transcript = transcript;
          markerToUpload = marker;
        }
      });
      this.setState({
        newMarkerInProgress: null
      });

      const formData = new FormData();
      formData.append('file', markerToUpload.audioBlob);
      formData.append('name', 'test pin');
      formData.append('latitude', markerToUpload.position.lat);
      formData.append('longitude', markerToUpload.position.lng);
      formData.append('transcript', markerToUpload.transcript);
      formData.append('image', new File([""], "filename"));
      formData.append('title', 'test title');
      formData.append('isComplaint', false);

      const url = `${API}/${this.state.userId}/save`;

      fetch(url, {
        method: 'POST',
        body: formData
      })
        .then(function(response) {
            if (response.status >= 400) {
              throw new Error("Bad response from server");
            }
            return response.json();
        })
        .then(function(data) {
            console.log('++++++++++!+ response from server is', data);
        })
        .catch((err) => {
          console.log('---------- error uploading pin to server');
        })

      // if(!mobileRepeatBug) {
      //   noteContent += transcript;
      //   noteTextarea.val(noteContent);
      // }
    };
    this.recognition.onstart = () => {
      console.log('onstart..');
    }

    this.recognition.onspeechend = () => {
      console.log('onspeechend..');
      const { newMarkerInProgress } = this.state;
      if (newMarkerInProgress !== null) {
        this.setState({
          newMarkerInProgress: null
        });
      }
    }

    this.recognition.onerror = function(event) {
      console.log('onerror', event);
    }
  };

  componentWillMount = () => {
    this.setupSpeechRecognition();
    this.loadUsers();
  }

  loadUsers = () => {
    const url = `${API}/admin/user`;
    fetch(url, {
      method: 'GET'
    })
      .then((response) => response.json())
      .then((response) => {
        this.setState({
          userList: response
        });
        this.loadPins(this.state.userId);
      })
      .catch((err) => {
        console.log('error getting user list', err);
      });
  };

  loadPins = (userid) => {
    const lat = 26.35869;
    const long = -80.0831;
    const url = `${API}/${this.state.userId}/geo?latitude=${lat.toString()}&longitude=${long.toString()}`;
    fetch(url, {
      method: 'GET'
    })
      .then(function(response) {
          if (response.status >= 400) {
            throw new Error("Bad response from server");
          }
          return response.json();
      })
      .then((data) => {
          if (data) {
            const markers = Object.keys(data).map((key) => {
              const item = data[key];
              const ret = {
                position: {
                  lat: item.latitude,
                  lng: item.longitude
                },
                recId: item.recId,
                isPrivate: item.isPrivate
              };
              return ret;
            });
            this.setState({
              markers
            });
          }
      })
      .catch((err) => {
        console.log('-----> error loading pins from server');
      })
  };

  stopRecording = () => {
    clearInterval(this.myTimer);
    this.myTimer = null;
    this.setState({
      showTimer: false
    });
    this.currentSeconds = 0;
    if (this.mediaRecorder) {
      this.mediaRecorder.stop();
    }
  };

  startTimer = () => {
    this.currentSeconds = 0;
    this.myTimer = setInterval(() => {
      const el = document.getElementById('time');
      el.innerHTML = this.currentSeconds < 10 ?
        "0:0".concat(this.currentSeconds) : "0:".concat(this.currentSeconds);
      this.currentSeconds++;
    }, 1000);
  };

  startRecording = (pinId) => {
    this.setState({
      showTimer: true
    });
    this.startTimer();
    this.recognition.start();
    window.navigator.mediaDevices.getUserMedia({ audio: true })
      .then(stream => {
        const mediaRecorder = new MediaRecorder(stream);
        this.mediaRecorder = mediaRecorder;
        this.mediaRecorder.start();

        this.audioChunks = [];
        this.mediaRecorder.addEventListener("dataavailable", event => {
          this.audioChunks.push(event.data);
        });

        mediaRecorder.addEventListener("stop", () => {
          const audioBlob = new Blob(this.audioChunks);
          this.audioBlob = audioBlob;
          const audioUrl = URL.createObjectURL(audioBlob);
          const audio = new Audio(audioUrl);
          this.audio = audio;

          const {
            markers,
            newMarkerInProgress,
            newMarkerInProgressId
          } = this.state;
          markers.forEach((marker) => {
            if (marker.id === newMarkerInProgressId) {
              marker.audioBlob = this.audioBlob;
            }
          });

          this.setState({
            newMarkerInProgress: {
              ...this.state.newMarkerInProgress,
              audioBlob: this.audioBlob
            }
          }, () => {
            this.recognition.stop();
          });

          this.audioBlob = null;
          this.audio = null;
          this.audioChunks = null;
        });

        // setTimeout(() => {
        //   this.mediaRecorder.stop();
        // }, 3000);
      });
  };

  onMarkerClick = (props, marker, e) => {
    this.setState({
      selectedPlace: props,
      activeMarker: marker,
      showingInfoWindow: true
    });
  };

  saveAs = (blob, fileName) => {
    var url = window.URL.createObjectURL(blob);

    var anchorElem = document.createElement("a");
    anchorElem.style = "display: none";
    anchorElem.href = url;
    anchorElem.download = fileName;

    document.body.appendChild(anchorElem);
    anchorElem.click();

    document.body.removeChild(anchorElem);

    // On Edge, revokeObjectURL should be called only after
    // a.click() has completed, atleast on EdgeHTML 15.15048
    setTimeout(function() {
        window.URL.revokeObjectURL(url);
    }, 1000);
}

  clickMarker = (marker) => {
    this.setState({
      showMarkerDetails: marker
    });
    // console.log('click the marker..', marker);
    if (!marker.recId || marker.audioBlob) {
      return;
    }
    // console.log('trigger download..', marker.recId);
    // marker.recId = '5d9957a795fe130001025b38';
    const url = `${API}/download/${marker.recId}`;
    // this.setState({
    //   audioSource: url
    // });
    const audioTest = document.getElementById('audioTest');
    audioTest.setAttribute('src', url);
    // console.log('source is..', audioTest.src);

    // const audioUrl = URL.createObjectURL(audioTest.src);
    // this.audio = new Audio(audioTest.src);
    // this.audio.play();

    const { markers } = this.state;
    Object.keys(markers).forEach((key) => {
      const item = markers[key];
      if (item.recId === marker.recId) {
        // console.log('UPDATE DONE');
        item.audioBlobExpress = audioTest.src;
      }
    });
    // console.log('marker is..', marker, 'and', markers);

    return;
    fetch(url, {
      method: 'GET'
    })
      .then((response) => {
        // if (response.status >= 400) {
        //   throw new Error("Bad response from server");
        // }
        // this.setState({
        //   audioSource: response
        // });
        console.log('blob 1');
        // var blob = new Blob([response], {type: 'application/octet-stream'});
        // console.log('blob 2', blob);

        // const audioUrl = URL.createObjectURL(response);
        // console.log('blob 3');
        // this.saveAs(blob, 'new file');
        // this.audio = new Audio(response);
        // console.log('blob ');

        // this.audio.play();
        // console.log('blob playing', blob);
        // return response.json();
      })
      .catch((err) => {
        console.log('---------- detailed data response error');
      })
    // if (marker && marker.audioBlob){
    //   const audioUrl = URL.createObjectURL(marker.audioBlob);
    //   const audio = new Audio(audioUrl);
    //   audio.play();
    // }
  };

  onClose = props => {
    if (this.state.showingInfoWindow) {
      this.setState({
        showingInfoWindow: false,
        activeMarker: null
      });
    }
  };

  getMyLocation() {
    const location = window.navigator && window.navigator.geolocation;
    if (location) {
      console.log('getting current location', location);
      location.getCurrentPosition((position) => {
        this.setState({
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
        })
      }, (error) => {
        console.log('error location', error);
        // this.setState({ latitude: 'err-latitude', longitude: 'err-longitude' })
      })
    }
  }

  createPinAtCenter = () => {
    console.log('get map coordinates...', this.props);
  }
  // getMyLocation = (e) => {
  //   let location = null;
  //   let latitude = null;
  //   let longitude = null;
  //   if (window.navigator && window.navigator.geolocation) {
  //       location = window.navigator.geolocation;
  //   }
  //   if (location){
  //       location.getCurrentPosition(function (position) {
  //           latitude = position.coords.latitude;
  //           longitude= position.coords.longitude;
  //           console.log('latitude', latitude);
  //           console.log('longitude', longitude);
  //       })
  //   }
  //   this.setState({latitude: latitude, longitude: longitude})
  // }

  mapClicked = (mapProps, map, event) => {
    // console.log('map clicked..', event);
    const lat = event.latLng.lat();
    const lng = event.latLng.lng();
    console.log('lat lng', lat, lng);
    const { markers, showMarkerDetails } = this.state;
    if (showMarkerDetails) {
      this.setState({
        showMarkerDetails: false
      });
    }
    console.log('this audio is..', this.audio);
    if (this.audio) {
      console.log('stopping audio...');
      this.audio.pause();
    }



    // this.setState({
    //   markers,
    //   newMarkerInProgress: {
    //     position: {
    //       lat,
    //       lng
    //     }
    //   }
    // });
  };

  createPinClick = () => {
    // console.log('map clicked..', event);
    // const lat = event.latLng.lat();
    // const lng = event.latLng.lng();
     
    const lat = 26.404680111911883;
    const lng = -80.1201275378246;
    const { markers } = this.state;
    const pinId = uuidv4();
    const newPin = {
      name: 'test',
      id: pinId,
      position: {
        lat,
        lng
      },
      isPrivate: true
    };
    markers.push(newPin);
    this.setState({
      markers,
      newMarkerInProgressId: pinId,
      newMarkerInProgress: {
        ...newPin
      }
    });

    this.startRecording(pinId);
  };

  closeShowMarkerDetails = () => {
    this.setState({
      showMarkerDetails: null
    });
  };

  saveNewPin = () => {
    let {
      newMarkerInProgress,
      markers
    } = this.state;

    const newPin = {
      ...newMarkerInProgress,
      name: "Current position",
      position: {
        lat: newMarkerInProgress.position.lat,
        lng: newMarkerInProgress.position.lng
      },
      audioBlob: newMarkerInProgress.audioBlob,
      transcript: newMarkerInProgress.transcript
    };



    // after server success response...
    markers.push(newPin);

    this.setState({
      markers,
      newMarkerInProgress: null
    });
  };

  cancelNewPin = () => {
    this.setState({
      newMarkerInProgress: null
    });
  };

  playRecording = () => {
    const {
      showMarkerDetails
    } = this.state;

    console.log('abcd...', this.state.showMarkerDetails);
    if (showMarkerDetails && showMarkerDetails.audioBlob) {
      console.log('whats up 1');
      const audioUrl = URL.createObjectURL(showMarkerDetails.audioBlob);
      this.audio = new Audio(audioUrl);
      this.audio.play();
    } else if (showMarkerDetails && showMarkerDetails.audioBlobExpress) {
      console.log('whats up 2');
      this.audio = new Audio(showMarkerDetails.audioBlobExpress);
      this.audio.play();
    }

    // this.setState({
    //   audioPaused: false
    // });
    // this.audio = null;
  };

  // pauseRecording = () => {
  //   if (this.audio) {
  //     this.audio.pause();
  //     this.setState({
  //       audioPaused: true
  //     });
  //   }
  // };

  changeSearchTag = (e) => {
    this.setState({
      searchTag: e.target.value
    });
  };

  render() {
    const {
      newMarkerInProgress,
      showMarkerDetails,
      markers,
      showTimer
    } = this.state;
    console.log('props are', this.props);
    console.log('Markers is', markers);

    return (
      <div>        
        <div id="mapContainer">
          {/*<div className="searchContainer">
            <input type="text" onChange={this.changeSearchTag} value={this.state.searchTag} />
            <button><img src={FiltersSearch} /></button>
          </div>*/}
          <Map
            google={this.props.google}
            zoom={14}
            style={mapStyles}
            initialCenter={{
             lat: 26.404680111911883,
             lng: -80.1201275378246
            }}
            streetViewControl={false}
            fullscreenControl={false}
            zoomControl={false}
            mapTypeControl={false}
            onClick={this.mapClicked}
          >
            {this.state.markers.map((marker, index) => {
              console.log('marker is private', marker.isPrivate);
              return marker.isPrivate ? (
                <Marker
                  key={index}
                  position={marker.position}
                  onClick={() => { this.clickMarker(marker); }}
                  name={marker.name}
                  icon={{
                    url: "./images/pinYellow.png",
                    anchor: new google.maps.Point(10, 10),
                    scaledSize: new google.maps.Size(40, 40)
                  }}
                />
              ) : (
                <Marker
                  key={index}
                  position={marker.position}
                  onClick={() => { this.clickMarker(marker); }}
                  name={marker.name}
                  icon={{
                    url: "./images/pinBlue.png",
                    anchor: new google.maps.Point(10, 10),
                    scaledSize: new google.maps.Size(40, 40)
                  }}
                />
              );
            })}
          </Map>
          <button
            className={showMarkerDetails ? 'Play' : (newMarkerInProgress ? 'Stop' : 'Add')}
            onClick={showMarkerDetails ? this.playRecording : (newMarkerInProgress ? this.stopRecording : this.createPinClick)}
            id="getCurrentPosition"
          >
          </button>
          { showTimer ? (
            <div id="newMarkerInfo">
              <p id="time"></p>
            </div>
          ) : null
          }
            
          { showMarkerDetails ? (
            <div id="detailedMarkerInfo">
              <p className="detailedViewName">{this.state.user}</p>
              { showMarkerDetails.transcript ?
                <p className="detailedViewName detailedViewName-transcript">{ showMarkerDetails.transcript }</p> : null
              }

              {/* <button className="closeDetailedView" onClick={this.closeShowMarkerDetails}>x</button> */}
            </div>
            ) : null
          }
          <button onClick={this.clickMarker}>DOWNLOAD TEST</button>
          <div className="bottom-navigation">
            <button className="DiscoverDisabled btn" onClick={() => { this.props.history.push('/discover', { userId: this.state.userId }); }} />
            <button className="MapsDisabled btn" onClick={() => { this.loadPins(this.state.userId); this.props.history.push('/maps'); }}></button>
            <button className="ProfileDisabled btn" onClick={() => { this.props.history.push('/profile'); }}></button>
          </div>
        </div>


        <audio style={{opacity: '0'}} id="audioTest" src="" controls></audio>

        

      </div>
    );
  }
}

export default GoogleApiWrapper({
  apiKey: 'your api key'
})(HomePage);
