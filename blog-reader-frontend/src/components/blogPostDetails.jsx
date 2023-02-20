import {Component} from "react";
import DOMPurify from "dompurify";

/**
 * Component to show the blogpost-details.
 */
export default class BlogPostDetails extends Component{

    /**
     * Sets the Posttitle as a link and header and inserts the original content-html to allow reading the post
     * Uses DOMPurify to remove potentially malicious elements
     * @returns {JSX.Element}
     */
    render(){
        const {blogPost} = this.props;
        const safeHtml = DOMPurify.sanitize(blogPost.content)
        return(
            <div data-testid='blogPostDetails'>
                <h1><a href={blogPost.link}>{blogPost.title}</a></h1>
                <div dangerouslySetInnerHTML={{__html:safeHtml}} />
            </div>
        );
    }
}