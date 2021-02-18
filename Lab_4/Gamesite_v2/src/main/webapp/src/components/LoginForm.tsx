import { Component } from 'react'
import Button from 'react-bootstrap/Button';

interface Props {
    handleCreateAccount: () => void;
}

type State = {
    username: string;
    password: string;
    remember: boolean;
}

class LoginForm extends Component<Props,State> {
    state = {
        username: '',
        password: '',
        remember: false
    }


    handleUsernameChange = (e: React.FormEvent<HTMLInputElement>): void => {
        this.setState({ username: e.currentTarget.value });
    } 

    handlePasswordChange = (e: React.FormEvent<HTMLInputElement>): void => {
        this.setState({ password: e.currentTarget.value });
    } 

    handleRememberChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
        this.setState({ remember: e.target.checked });
    } 

    handleSubmit = (): void => {
        alert(`${this.state.username} ${this.state.password} ${this.state.remember}`)
    } 
    
    render() {
        return(
            <form className="login-form" onSubmit={this.handleSubmit}>
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
                    <input name="Remember me" type="checkbox" id="checkbox" checked={this.state.remember} onChange={this.handleRememberChange} />
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




