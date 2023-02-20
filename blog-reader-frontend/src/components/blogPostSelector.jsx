import React,{Component} from "react";

/**
 * Represents a single Blogpost as a Button that shows title and excerpt of the post.
 */
export default class BlogPostSelector extends Component{
    render(){
        const{props} = this;
        return(
            <React.Fragment>
                <button onClick={() => props.handleSelection(this.props.index)}
                        data-testid={`postListItem-${this.props.index}`}>
                    <h4>{props.blogPost.title}</h4>
                    <div dangerouslySetInnerHTML={{__html:props.blogPost.excerpt}} />
                </button>
            </React.Fragment>
        );
    }
}