import React from "react";
import ReactDOM from "react-dom";
import {Container, Dropdown, Icon, Input, Menu} from "semantic-ui-react";

import Example from "./search";
import Rated from "./rated";
import client from "./client";

class App extends React.Component {
    state = {
        activeItem: 'home',
        typing: '',
        userName: '',
        nameChanging: false,
        query: '',
        results: [],
        rated: []
    };

    updateRate(i, rating) {
        this.setState((state) => {
            const results = state.results;
            results[i].user_rating = rating;
            return {results: results};
        });
    }

    updateRatedRate(i, rating) {
        this.setState((state) => {
            const results = state.rated;
            results[i].user_rating = rating;
            return {rated: results};
        });
    }

    updateQuery(query) {
        this.setState({query});
    }

    updateResults(results) {
        this.setState({results});
    }

    updateRatedResults(results) {
        this.setState({rated: results});
    }

    handleNameChange = (e) => {
        this.setState({typing: e.target.value});
    };

    handleNameSubmit = () => {
        if (this.state.userName !== this.state.typing) {
            this.setState({nameChanging: true});
            client({
                method: 'POST',
                path: '/change_name?user_name=' + encodeURIComponent(this.state.typing)
            }).then(response => {
                console.log(response);
                this.setState({
                    nameChanging: false,
                    userName: this.state.typing,
                    query: '',
                    results: [],
                    rated: []
                });

            }).catch(error => {
                // do things with the error, like logging them:
                console.error(error);
                this.setState({nameChanging: false});
            });
            client({method: 'GET', path: '/rated_recipes'}).then(response => {
                console.log(response);
                this.updateRatedResults(response.entity);
            }).catch(error => {
                // do things with the error, like logging them:
                console.error(error)
            });
        }
    };

    handleItemClick = (e, {name}) => this.setState({activeItem: name});

    render() {
        const trigger = (
            <span>
    <Icon name='user'/> {this.state.userName ? 'Hello, ' + this.state.userName : 'Sign In'}
  </span>
        );
        const {activeItem} = this.state;
        return (<Container style={{margin: 20}}>
            <Menu>
                <Menu.Item header>Search Food Recipe</Menu.Item>
                <Menu.Item
                    name='home'
                    active={activeItem === 'home'}
                    onClick={this.handleItemClick}
                />
                <Menu.Item
                    name='rated recipes'
                    active={activeItem === 'rated'}
                    onClick={this.handleItemClick}
                    disabled={this.state.userName.length === 0}
                />

                <Menu.Menu position='right'>

                    <Dropdown item trigger={trigger}>
                        <Dropdown.Menu>
                            <Input action={{color: 'blue', content: 'Change', onClick: () => this.handleNameSubmit()}}
                                   placeholder='Change user...'
                                   onChange={this.handleNameChange.bind(this)}
                                   onClick={e => e.stopPropagation()}/>
                        </Dropdown.Menu>
                    </Dropdown>
                </Menu.Menu>
            </Menu>

            {(activeItem === 'home') ?
                <Example query={this.state.query}
                         results={this.state.results}
                         updateQuery={this.updateQuery.bind(this)}
                         updateResults={this.updateResults.bind(this)}
                         canRate={this.state.userName.length > 0}
                         updateRate={this.updateRate.bind(this)}/> :
                <Rated results={this.state.rated}
                       updateRatedResults={this.updateRatedResults.bind(this)}
                       updateRatedRate={this.updateRatedRate.bind(this)}
                />}
        </Container>);
    }
}

// TODO: Switch to https://github.com/palmerhq/the-platform#stylesheet when it will be stable
const styleLink = document.createElement("link");
styleLink.rel = "stylesheet";
styleLink.href = "https://cdn.jsdelivr.net/npm/semantic-ui/dist/semantic.min.css";
document.head.appendChild(styleLink);

ReactDOM.render(
    <App>
    </App>,
    document.getElementById("react")
);
