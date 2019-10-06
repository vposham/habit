/* eslint-disable */

import React from 'react';
import './LoginPage.scss';
import Logo from './Logo.svg';
import Constants from './constants';
import FacebookImg from './images/Facebook.png';
import GmailImg from './images/Gmail.png';
import TwitterImg from './images/Twitter.png';


const API = Constants.API;

class LoginPage extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			username: '',
			password: ''
		};
	}

	login = (username, password) => {
		const url = `${API}/validate`;
		fetch(url, {
      method: 'POST',
      body: JSON.stringify({
      	username: username,
      	password: password
      }),
      headers: {
	      'Content-Type': 'application/json'}
    })
      .then((response) => response.json())
      .then((response) => {
        this.props.history.push("/discover", { userId: response.userId })
        // this.props.history.push("/maps", { userId: response.userId })
      })
      .catch((err) => {
        console.log('error getting user list', err);
      });
	}

	render() {
		const { history } = this.props;
		return (
			<div className="loginPage">
				<div className="logo">
					<object data={Logo} type="image/svg+xml" />
				</div>
				<button onClick={() => { this.login('private', 'Tester123'); } } className="spbtn">
					<img className="facebookBtn" src={FacebookImg} />
				</button>
				<button onClick={() => { this.login('public', 'Tester123'); } } className="spbtn">
					<img className="facebookBtn" src={GmailImg} />
				</button>
				<button onClick={() => { this.login('dilip1', 'Tester123'); } } className="spbtn">
					<img className="facebookBtn" src={TwitterImg} />
				</button>
			</div>
		);
	}
};

export default LoginPage;
