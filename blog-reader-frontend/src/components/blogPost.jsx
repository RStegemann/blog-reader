import {Component} from "react";

export default class BlogPost extends Component{
    /*state = {
        id: this.props.blogPost.id,
        title: this.props.blogPost.title,
        content: this.props.blogPost.content,
        excerpt: this.props.blogPost.excerpt,
        link: this.props.blogPost.link,
        wordMap: this.props.blogPost.wordMap
    };*/

    render(){
        return(
            <div>
                <h4><a href={this.props.blogPost.link}>{this.props.blogPost.title}</a></h4>
                <div dangerouslySetInnerHTML={{__html:this.props.blogPost.excerpt}} />
            </div>
        );
    }
}