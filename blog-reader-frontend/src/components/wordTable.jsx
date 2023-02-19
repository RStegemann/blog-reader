import {Component} from "react";

export default class WordTable extends Component{

    render(){
        return(
            <div>
                <table>
                    <tr><th>Wort</th><th>Anzahl</th></tr>
                    {this.props.wordCountArray.map(wordCount =>
                        <tr>
                            <td>
                                {wordCount[0]}
                            </td>
                            <td>
                                {wordCount[1]}
                            </td>
                        </tr>
                    )}
                </table>
            </div>
        );
    }
}