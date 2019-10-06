/* eslint-disable */

import React from 'react';
import './Discovery.scss';
// import OfficeDepot from 'OfficeDepot.m4a';
// import SingingPines from 'SingingPines.mp3';

class Discovery extends React.Component {
  playOD = () => {
  	if (this.audio) {
  		this.audio.stop();
  	}
  	const audioTest = document.getElementById('odClip');
    // audioTest.setAttribute('src', url);
    // console.log('source is..', audioTest.src);

    // const audioUrl = URL.createObjectURL(audioTest.src);
    this.audio = new Audio(audioTest.src);
    this.audio.play();
  };

  stopAudio = () => {
  	if (this.audio) {
  		this.audio.stop();
  	}
  };

  render() {
  	const userId = this.props.location && this.props.location.state ? this.props.location.state.userId : null;
    return (
      <div>
        <button onClick={this.playOD} className="imageGen image1" />
        <audio id="odClip" src="./images/OfficeDepot.m4a" />
        <audio id="singingPines" src="./images/SingingPines.mp3" />
        <button className="imageGen image2" />
        <button className="imageGen image3" />
        <button className="imageGen image4" />
        <button className="imageGen image5" />
        <div style={{ top: '0px'}} className="bottom-navigation">
	        <button className="DiscoverDisabled btn" onClick={() => { this.props.history.push('/discover', { userId: userId }); }} />
	        <button className="MapsDisabled btn" onClick={() => { this.props.history.push('/maps', { userId: userId }); }}></button>
	        <button className="ProfileDisabled btn" onClick={() => { this.props.history.push('/profile', { userId: userId }); }}></button>
	      </div>
      </div>
    );
  }
};

export default Discovery;
