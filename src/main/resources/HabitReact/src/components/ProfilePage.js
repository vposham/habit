/* eslint-disable */

import React from 'react';
import ProfileImg from './images/ProfilePage.png';

class ProfilePage extends React.Component {
  render() {
    let userId = null;
    if (this.props.location && this.props.location.state && this.props.location.state.userId) {
      userId = this.props.location.state.userId;
    }

    return (
      <div className="profile-page">
        <img src={ProfileImg} />
        <div style={{ top: '-110px', position: 'relative' }}>
	        <button className="DiscoverDisabled btn" onClick={() => { this.props.history.push('/discover'); }}>
	        </button>
	        <button className="MapsDisabled btn" onClick={() => { this.props.history.push('/maps', { userId: userId }); }}></button>
	        <button className="ProfileDisabled btn" onClick={() => { this.props.history.push('/profile', { userId: userId }); }}></button>
	      </div>
      </div>
    );
  }
};

export default ProfilePage;
