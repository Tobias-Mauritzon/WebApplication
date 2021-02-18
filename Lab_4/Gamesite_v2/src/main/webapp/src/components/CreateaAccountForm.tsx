import { Component } from 'react'
import Button from 'react-bootstrap/Button';

type State = {
    username?: string;
    email?: string;
    password?: string;
    passwordRepeat?: string;
};

class CreateaAccountForm extends Component<State> {
    state = {
        username: '',
        email: '',
        password: '',
        passwordRepeat: ''
    }

    handleUsernameChange = (e: React.FormEvent<HTMLInputElement>): void => {
        this.setState({ username: e.currentTarget.value });
    } 

    handleEmailChange = (e: React.FormEvent<HTMLInputElement>): void => {
        this.setState({ email: e.currentTarget.value });
    } 

    handlePasswordChange = (e: React.FormEvent<HTMLInputElement>): void => {
        this.setState({ password: e.currentTarget.value });
    } 

    handlePasswordRepeatChange = (e: React.FormEvent<HTMLInputElement>): void => {
        this.setState({ passwordRepeat: e.currentTarget.value });
    } 

    handleSubmit = (): void => {
        alert(`${this.state.username}  ${this.state.email}  ${this.state.password}  ${this.state.passwordRepeat}`)
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
                    <label className="">Email</label>
                </div>
                <div className="mt-1">
                    <input className="login-textbox" type="email" value={this.state.email} onChange={this.handleEmailChange}/>
                </div>
                <div className="mt-1">
                    <label className="">Password</label>
                </div>
                <div className="mt-1">
                    <input className=" login-textbox" type="password"  onChange={this.handlePasswordChange}/>
                </div>
                <div className="mt-1">
                    <label className="">Repeat Password</label>
                </div>
                <div className="mt-1">
                    <input className=" login-textbox" type="password"  onChange={this.handlePasswordRepeatChange}/>
                </div>
                <div className="mt-4 mb-3">
                    <Button variant="primary" className="button btn-max-width" type="submit">Create Account</Button>  
                </div>
            </form>
        )
    }
}
export default CreateaAccountForm




