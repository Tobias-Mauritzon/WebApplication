import { Component } from 'react'
import Button from 'react-bootstrap/Button';

class LoginForm extends Component {
    constructor(props) {
        super(props)

        this.state = {
            username: '',
            password: '',
            remember: false
        }
    }

    handleUsernameChange = (event) => {
        this.setState({
            username: event.target.value
        })
    } 

    handlePasswordChange = (event) => {
        this.setState({
            password: event.target.value
        })
    }

    handleRememberChange = (event) => {
        this.setState({
            remember: event.target.value
        })
    }

    handleSubmit = (event) => {
        alert(`${this.state.username} ${this.state.password} ${this.state.remember}`)
    }
    
    render() {
        return(
            <form class="login-form" onSubmit={this.handleSubmit}>
                <div className="mt-2">
                    <label className="">Username</label>
                </div>
                <div className="mt-1">
                    <input className="login-textbox" type="text" value={this.state.username} onChange={this.handleUsernameChange}/>
                </div>
                <div className="mt-1">
                    <label className="">Password</label>
                </div>
                <div className="mt-1">
                    <input className=" login-textbox" type="password"  onChange={this.handlePasswordChange}/>
                </div>
                <div className="mt-2">
                    <label>Remember me</label>
                    <input name="Remember me" type="checkbox" checked={this.state.remember} onChange={this.handleRememberChange} />
                </div>
                <div className="row mt-2 mb-3">
                    <div className="col-6" id="popup-login-buttons-left">
                        <Button variant="primary" className="button btn-max-width" onClick={this.props.handleCreateAccount}>create account</Button>{' '}  
                    </div>
                    <div className="col-6" id="popup-login-buttons-right">
                        <Button variant="primary" className="button btn-max-width" type="submit">Login</Button>  
                    </div>
                </div>
            </form>
        )
    }
}
export default LoginForm




