import React,{Component} from "react";

/**
 * Creates a table containing the columns 'word' and 'count' and fills it with data from the wordcount array of the
 * selected post
 */
export default class WordTable extends Component{

    render(){
        return(
            <React.Fragment>
                <table data-testid='wordTable'>
                    <tbody>
                        <tr><th>Wort</th><th>Anzahl</th></tr>
                        {this.props.wordCountArray.map(wordCount =>
                            <tr key={this.props.wordCountArray.indexOf(wordCount)}>
                                <td data-testid={`wordCell-${wordCount[0]}`}>
                                    {wordCount[0]}
                                </td>
                                <td data-testid={`countCell-${wordCount[0]}`}>
                                    {wordCount[1]}
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </React.Fragment>
        );
    }
}