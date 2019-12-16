import React, {Component} from 'react'
import {
    Dimmer,
    Grid,
    Header,
    Icon,
    Image,
    Input,
    Label,
    List,
    Loader,
    Modal,
    Rating,
    Search,
    Segment,
    Table
} from 'semantic-ui-react'
import client from './client'


export default class RatedRecipes extends Component {
    constructor(props) {
        super(props);
        this.state = {isLoading: true};
        client({method: 'GET', path: '/rated_recipes'}).then(response => {
            console.log(response);
            this.setState({isLoading: false});
            this.props.updateRatedResults(response.entity);
        }).catch(error => {
            // do things with the error, like logging them:
            console.error(error)
        });
    }

    handleRate = (index, id, e, {rating}) => {
        this.props.updateRatedRate(index, rating);
        client({method: 'POST', path: '/rate?recipe_id=' + id + '&rating=' + rating}).then(response => {
            console.log(response);
        }).catch(error => {
            // do things with the error, like logging them:
            console.error(error)
        });
    };

    componentDidMount() {
        client({method: 'GET', path: '/rated_recipes'}).then(response => {
            console.log(response);
            this.setState({isLoading: false});
            this.props.updateRatedResults(response.entity);
        }).catch(error => {
            // do things with the error, like logging them:
            console.error(error)
        });
    }

    renderResults() {
        const color = {
            '-1': 'grey',
            '0': 'red',
            '1': 'orange',
            '2': 'yellow',
            '3': 'olive',
            '4': 'green',
            '5': 'teal'
        };
        return <Segment raised disabled={this.props.results.length === 0}>
            {this.props.results.length > 0 ?
                <List divided animated selection verticalAlign='middle'>
                    {this.props.results.map((res, i) => {
                        return (
                            <Modal key={i} trigger={
                                <List.Item key={i}>
                                    <List.Content floated='right'>
                                        <Label>{res.minutes + ' min.'}</Label>
                                        <Label color={color[Math.round(res.user_rating)]}>
                                            <Icon name='star'/>{res.user_rating}
                                        </Label>
                                    </List.Content>
                                    <List.Icon name='food' color={color[res.user_rating]}/>
                                    <List.Content>
                                        <List.Header as='a'>{res.name.split(' ')
                                            .map((s) => s.charAt(0).toUpperCase() + s.substring(1))
                                            .join(' ')}
                                        </List.Header>
                                        <List.Description>
                                            {res.description}
                                        </List.Description>
                                    </List.Content>
                                </List.Item>
                            } closeIcon>
                                <Modal.Header icon='food'>{res.name.split(' ')
                                    .map((s) => s.charAt(0).toUpperCase() + s.substring(1))
                                    .join(' ')}</Modal.Header>
                                <Modal.Content scrolling>
                                    <Modal.Description>
                                        <Header>Description</Header>
                                        <p>
                                            {res.description}
                                        </p>
                                        <Header>Ingredients</Header>
                                        <List bulleted>
                                            {res.ingredients.map((ingre, j) => <List.Item key={j}>{ingre}</List.Item>)}
                                        </List>

                                        <Header>Steps</Header>
                                        <List ordered>
                                            {res.steps.map((step, j) => <List.Item key={j}>{step}</List.Item>)}
                                        </List>

                                        <Header>Nutrition</Header>

                                        <Table celled striped collapsing>
                                            <Table.Header>
                                                <Table.Row>
                                                    <Table.HeaderCell colSpan='2'>Nutrition Facts</Table.HeaderCell>
                                                </Table.Row>
                                            </Table.Header>

                                            <Table.Body>
                                                <Table.Row>
                                                    <Table.Cell collapsing>Calories</Table.Cell>
                                                    <Table.Cell textAlign='right'>{res.nutrition[0]}</Table.Cell>
                                                </Table.Row>

                                                <Table.Row>
                                                    <Table.Cell collapsing>Total Fat</Table.Cell>
                                                    <Table.Cell textAlign='right'>{res.nutrition[1]}</Table.Cell>
                                                </Table.Row>

                                                <Table.Row>
                                                    <Table.Cell collapsing>Sugar</Table.Cell>
                                                    <Table.Cell textAlign='right'>{res.nutrition[2]}</Table.Cell>
                                                </Table.Row>

                                                <Table.Row>
                                                    <Table.Cell collapsing>Sodium</Table.Cell>
                                                    <Table.Cell textAlign='right'>{res.nutrition[3]}</Table.Cell>
                                                </Table.Row>

                                                <Table.Row>
                                                    <Table.Cell collapsing>Protein</Table.Cell>
                                                    <Table.Cell textAlign='right'>{res.nutrition[4]}</Table.Cell>
                                                </Table.Row>

                                                <Table.Row>
                                                    <Table.Cell collapsing>Saturated Fat</Table.Cell>
                                                    <Table.Cell textAlign='right'>{res.nutrition[5]}</Table.Cell>
                                                </Table.Row>

                                                <Table.Row>
                                                    <Table.Cell collapsing>Carbohydrates</Table.Cell>
                                                    <Table.Cell textAlign='right'>{res.nutrition[6]}</Table.Cell>
                                                </Table.Row>
                                            </Table.Body>
                                        </Table>

                                        <Header>Tags</Header>
                                        <Label.Group color='blue'>
                                            {res.tags.map((t, j) => {
                                                return <Label tag key={j}>
                                                    {'#' + t}
                                                </Label>;
                                            })}
                                        </Label.Group>
                                    </Modal.Description>
                                </Modal.Content>
                                <Modal.Actions>
                                    <Rating maxRating={5} rating={res.user_rating}
                                            icon='star' size='large' onRate={this.handleRate.bind(this, i, res.id)}/>
                                </Modal.Actions>
                            </Modal>
                        );
                    })}
                </List> : 'No Rated Recipe Found'}
        </Segment>

    }

    loader() {
        return (
            <Segment>
                <Dimmer active inverted>
                    <Loader size='large'>Loading</Loader>
                </Dimmer>

                <Image src='https://react.semantic-ui.com/images/wireframe/paragraph.png'/>
            </Segment>
        );
    }

    render() {
        return (
            <Grid centered>
                <Grid.Column width={10} >
                    {this.state.isLoading ? this.loader() : this.renderResults()}
                </Grid.Column>
            </Grid>
        )
    }
}
