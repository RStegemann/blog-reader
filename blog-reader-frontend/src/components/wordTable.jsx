import {Component} from "react";

export default class WordTable extends Component{

    render(){
        return(
            <div>
                <table>
                    <tbody>
                        <tr><th>Wort</th><th>Anzahl</th></tr>
                        {this.props.wordCountArray.map(wordCount =>
                            <tr key={this.props.wordCountArray.indexOf(wordCount)}>
                                <td>
                                    {wordCount[0]}
                                </td>
                                <td>
                                    {wordCount[1]}
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        );
    }
}