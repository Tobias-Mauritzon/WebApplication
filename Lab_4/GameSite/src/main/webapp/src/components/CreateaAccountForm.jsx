import { Component } from 'react'
import Button from 'react-bootstrap/Button';

class CreateaAccountForm extends Component {
    constructor(props) {
        super(props)

        this.state = {
            username: '',
            email: '',
            password: '',
            passwordRepeat: ''
        }
    }

    handleUsernameChange = (event) => {
        this.setState({
            username: event.target.value
        })
    } 

    handleEmailChange = (event) => {
        this.setState({
            username: event.target.value
        })
    } 

    handlePasswordChange = (event) => {
        this.setState({
            password: event.target.value
        })
    }

    handlePasswordRepeatChange = (event) => {
        this.setState({
            passwordRepeat: event.target.value
        })
    }

    handleSubmit = (event) => {
        alert(`${this.state.username}  ${this.state.email}  ${this.state.password}  ${this.state.passwordRepeat}`)
    }
    
    render() {
        return(
            <form onSubmit={this.handleSubmit}>
                <div className="mt-2">
                    <label>Username</label>
                    <input type="text" value={this.state.username} onChange={this.handleUsernameChange}/>
                </div>
                <div className="mt-1">
                    <label>Email</label>
                    <input type="text"  onChange={this.handleEmailChange}/>
                </div>
                <div className="mt-1">
                    <label>Password</label>
                    <input type="password"  onChange={this.handlePasswordChange}/>
                </div>
                <div className="mt-1">
                    <label>Repeat Password</label>
                    <input type="password"  onChange={this.handlePasswordRepeatChange}/>
                </div>
                <div className="row">
                    <div className="col-6">
                        <Button className="button btn-max-width" type="submit">Login</Button>  
                    </div>
                </div>
            </form>
        )
    }
}
export default CreateAccountForm




